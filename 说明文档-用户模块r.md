# 有康系统 - 用户认证与权限机制详解

本文档详细说明有康管理系统中用户相关的核心功能，包括用户登录、信息存储、身份识别等机制。

## 📋 完整流程图

```
用户登录
   ↓
1. 验证码校验 + 用户名密码验证
   ↓
2. 生成JWT Token + UUID
   ↓
3. LoginUser对象存入Redis (key: login_tokens:uuid)
   ↓
4. 返回JWT Token给前端
   ↓
前端每次请求都带上Token (请求头: Authorization: Bearer xxx)
   ↓
5. JwtAuthenticationTokenFilter拦截请求
   ↓
6. 从Token解析UUID，从Redis取出LoginUser
   ↓
7. 放入Spring Security上下文
   ↓
8. 控制器通过SecurityUtils获取当前用户
```

---

## 🔐 1. 用户登录流程（存储用户信息）

### 1.1 登录入口

**接口：** `POST /login`

**控制器：** `SysLoginController.login()` (`youkang-admin/src/main/java/com/youkang/web/controller/system/SysLoginController.java:56`)

```java
@PostMapping("/login")
public AjaxResult login(@RequestBody LoginBody loginBody)
{
    // 调用登录服务
    String token = loginService.login(
        loginBody.getUsername(),
        loginBody.getPassword(),
        loginBody.getCode(),      // 验证码
        loginBody.getUuid()       // 验证码UUID
    );
    return AjaxResult.success().put(Constants.TOKEN, token);
}
```

**请求参数：**
```json
{
    "username": "admin",
    "password": "admin123",
    "code": "1234",
    "uuid": "验证码的UUID"
}
```

**响应结果：**
```json
{
    "code": 200,
    "msg": "操作成功",
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImExYjJjM2Q0LWU1ZjYtNzg5MC1hYmNkLWVmMTIzNDU2Nzg5MCJ9.xxx"
}
```

### 1.2 登录核心处理

**服务类：** `SysLoginService.login()` (`youkang-framework/src/main/java/com/youkang/framework/web/service/SysLoginService.java:63`)

```java
public String login(String username, String password, String code, String uuid)
{
    // ① 验证码校验（从Redis取验证码对比）
    validateCaptcha(username, code, uuid);

    // ② 登录前置校验（用户名密码格式、IP黑名单）
    loginPreCheck(username, password);

    // ③ 用户认证（调用Spring Security的authenticationManager）
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(username, password);

    // 这里会自动调用 UserDetailsServiceImpl.loadUserByUsername
    // 从数据库查询用户信息、角色、权限
    authentication = authenticationManager.authenticate(authenticationToken);

    // ④ 认证成功，获取LoginUser对象（包含用户信息+权限）
    LoginUser loginUser = (LoginUser) authentication.getPrincipal();

    // ⑤ 记录登录信息（更新数据库的最后登录时间、IP）
    recordLoginInfo(loginUser.getUserId());

    // ⑥ 生成Token并存储到Redis
    return tokenService.createToken(loginUser);
}
```

**登录前置校验内容：**
- 用户名、密码不能为空
- 用户名长度：2-20个字符
- 密码长度：5-20个字符
- IP黑名单校验（配置项：`sys.login.blackIPList`）

### 1.3 Token生成与Redis存储

**Token服务：** `TokenService.createToken()` (`youkang-framework/src/main/java/com/youkang/framework/web/service/TokenService.java:114`)

```java
public String createToken(LoginUser loginUser)
{
    // ① 生成UUID作为用户唯一标识
    String token = IdUtils.fastUUID();
    // 示例: "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
    loginUser.setToken(token);

    // ② 设置用户代理信息（IP、浏览器、操作系统、登录地点）
    setUserAgent(loginUser);

    // ③ 刷新Token并存入Redis（包含过期时间设置）
    refreshToken(loginUser);

    // ④ 生成JWT Token（只包含UUID和用户名，不包含敏感信息）
    Map<String, Object> claims = new HashMap<>();
    claims.put(Constants.LOGIN_USER_KEY, token);  // 存入UUID
    claims.put(Constants.JWT_USERNAME, loginUser.getUsername());

    return createToken(claims);  // 返回JWT字符串
}
```

**Redis存储：** `refreshToken()` (第148行)

```java
public void refreshToken(LoginUser loginUser)
{
    // 设置登录时间和过期时间
    loginUser.setLoginTime(System.currentTimeMillis());
    loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * 60 * 1000);

    // 存入Redis
    // key格式: "login_tokens:a1b2c3d4-e5f6-7890-abcd-ef1234567890"
    String userKey = getTokenKey(loginUser.getToken());

    // 完整的LoginUser对象被序列化存入Redis
    redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
}
```

**存储内容包括：**
- 用户ID、部门ID、用户名
- 完整的用户信息（SysUser对象）
- 权限列表（Set<String>）
- 登录IP、浏览器、操作系统、登录地点
- 登录时间、过期时间

---

## 🎯 2. 如何识别是哪个用户调用接口

### 2.1 请求拦截过滤器

**过滤器：** `JwtAuthenticationTokenFilter` (`youkang-framework/src/main/java/com/youkang/framework/security/filter/JwtAuthenticationTokenFilter.java:31`)

每个请求进来都会被这个过滤器拦截：

```java
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter
{
    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    {
        // ① 从请求中获取用户信息
        LoginUser loginUser = tokenService.getLoginUser(request);

        if (loginUser != null && SecurityUtils.getAuthentication() == null)
        {
            // ② 验证Token有效期（不足20分钟自动续期）
            tokenService.verifyToken(loginUser);

            // ③ 创建认证对象
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());

            // ④ 放入Spring Security上下文
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // ⑤ 继续过滤链
        chain.doFilter(request, response);
    }
}
```

### 2.2 Token解析流程

**Token解析：** `TokenService.getLoginUser()` (第62行)

```java
public LoginUser getLoginUser(HttpServletRequest request)
{
    // ① 从请求头获取Token
    // 请求头格式: Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
    String token = getToken(request);

    if (StringUtils.isNotEmpty(token))
    {
        // ② 解析JWT Token，获取UUID
        Claims claims = parseToken(token);
        String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);

        // ③ 根据UUID从Redis获取完整的LoginUser对象
        String userKey = getTokenKey(uuid);  // "login_tokens:uuid"
        LoginUser user = redisCache.getCacheObject(userKey);

        return user;  // 返回完整的用户信息
    }
    return null;
}
```

**流程说明：**
1. 前端每次请求都会在请求头中携带Token：`Authorization: Bearer <JWT Token>`
2. 过滤器从请求头提取Token
3. 解析JWT获取UUID（不涉及数据库查询）
4. 使用UUID从Redis查询完整的LoginUser对象
5. 将LoginUser放入Spring Security上下文
6. 后续代码可以随时从上下文获取当前用户

### 2.3 Token自动续期机制

**验证与续期：** `TokenService.verifyToken()` (第133行)

```java
public void verifyToken(LoginUser loginUser)
{
    long expireTime = loginUser.getExpireTime();
    long currentTime = System.currentTimeMillis();

    // 如果距离过期时间不足20分钟，自动刷新
    if (expireTime - currentTime <= MILLIS_MINUTE_TWENTY)
    {
        refreshToken(loginUser);  // 重新设置过期时间并更新Redis
    }
}
```

---

## 🔧 3. 在代码中如何获取当前用户

### 3.1 使用 SecurityUtils 工具类（推荐）

**工具类位置：** `com.youkang.common.utils.SecurityUtils`

```java
// ① 获取用户ID
Long userId = SecurityUtils.getUserId();

// ② 获取用户名
String username = SecurityUtils.getUsername();

// ③ 获取部门ID
Long deptId = SecurityUtils.getDeptId();

// ④ 获取完整的LoginUser对象
LoginUser loginUser = SecurityUtils.getLoginUser();

// ⑤ 获取SysUser对象（用户详细信息）
SysUser user = loginUser.getUser();

// ⑥ 获取用户权限列表
Set<String> permissions = loginUser.getPermissions();

// ⑦ 判断是否为管理员
boolean isAdmin = SecurityUtils.isAdmin(userId);

// ⑧ 检查用户是否有某个权限
boolean hasPerm = SecurityUtils.hasPermi("system:user:add");

// ⑨ 检查用户是否有某个角色
boolean hasRole = SecurityUtils.hasRole("admin");
```

### 3.2 直接从Spring Security获取

```java
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
LoginUser loginUser = (LoginUser) authentication.getPrincipal();
```

### 3.3 SecurityUtils 实现原理

**源码：** `SecurityUtils.getLoginUser()` (`youkang-common/src/main/java/com/youkang/common/utils/SecurityUtils.java:72`)

```java
public static LoginUser getLoginUser()
{
    try
    {
        // 从Spring Security上下文获取
        return (LoginUser) getAuthentication().getPrincipal();
    }
    catch (Exception e)
    {
        throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
    }
}

public static Authentication getAuthentication()
{
    // 从SecurityContextHolder获取认证信息
    return SecurityContextHolder.getContext().getAuthentication();
}
```

---

## 🗂️ 4. LoginUser 数据结构

**类位置：** `com.youkang.common.core.domain.model.LoginUser`

```java
public class LoginUser implements UserDetails
{
    // 基本信息
    private Long userId;              // 用户ID
    private Long deptId;              // 部门ID
    private String token;             // UUID标识

    // 登录相关
    private Long loginTime;           // 登录时间（毫秒时间戳）
    private Long expireTime;          // 过期时间（毫秒时间戳）

    // 登录环境信息
    private String ipaddr;            // 登录IP地址
    private String loginLocation;     // 登录地点（根据IP解析）
    private String browser;           // 浏览器类型
    private String os;                // 操作系统

    // 权限信息
    private Set<String> permissions;  // 权限列表（如：system:user:add）
    private SysUser user;             // 完整的用户信息对象

    // Spring Security接口方法
    public String getUsername() { return user.getUserName(); }
    public String getPassword() { return user.getPassword(); }
    public boolean isAccountNonExpired() { return true; }
    public boolean isAccountNonLocked() { return true; }
    public boolean isCredentialsNonExpired() { return true; }
    public boolean isEnabled() { return true; }
}
```

**SysUser 对象包含：**
- 用户基本信息（昵称、邮箱、手机号、性别、头像等）
- 所属部门信息
- 角色列表
- 岗位列表

---

## 💾 5. Redis存储结构

### 5.1 存储格式

```
Redis Key:   login_tokens:a1b2c3d4-e5f6-7890-abcd-ef1234567890
Redis Value: LoginUser对象（序列化后的字节流）
Redis TTL:   30分钟（默认配置）
```

### 5.2 配置项

**配置文件：** `application.yml`

```yaml
# token配置
token:
  # 令牌自定义标识（请求头名称）
  header: Authorization
  # 令牌密钥（用于JWT签名）
  secret: abcdefghijklmnopqrstuvwxyz
  # 令牌有效期（默认30分钟）
  expireTime: 30
```

### 5.3 Redis Key前缀

**常量定义：** `CacheConstants.LOGIN_TOKEN_KEY` = `"login_tokens:"`

完整Key示例：`login_tokens:a1b2c3d4-e5f6-7890-abcd-ef1234567890`

---

## 📊 6. 实际使用示例

### 6.1 示例1：控制器中获取当前用户

```java
@RestController
@RequestMapping("/system/customer")
public class CustomerController extends BaseController
{
    @Autowired
    private ICustomerService customerService;

    /**
     * 新增客户
     */
    @PostMapping
    @PreAuthorize("@ss.hasPermi('system:customer:add')")
    public AjaxResult add(@RequestBody Customer customer)
    {
        // 获取当前登录用户ID
        Long userId = SecurityUtils.getUserId();
        customer.setCreateBy(userId.toString());

        // 获取当前用户名
        String username = SecurityUtils.getUsername();
        customer.setCreateByName(username);

        // 获取当前用户部门ID（用于数据权限控制）
        Long deptId = SecurityUtils.getDeptId();
        customer.setDeptId(deptId);

        customer.setCreateTime(new Date());

        return toAjax(customerService.insertCustomer(customer));
    }

    /**
     * 查询客户列表（带数据权限）
     */
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:customer:list')")
    public TableDataInfo list(Customer customer)
    {
        startPage();  // 开启分页

        // 如果不是管理员，只能查看本部门的数据
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId()))
        {
            customer.setDeptId(SecurityUtils.getDeptId());
        }

        List<Customer> list = customerService.selectCustomerList(customer);
        return getDataTable(list);
    }
}
```

### 6.2 示例2：Service层中获取当前用户

```java
@Service
public class CustomerServiceImpl implements ICustomerService
{
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    @Transactional
    public int updateCustomer(Customer customer)
    {
        // 获取当前用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();

        // 数据权限校验：只能修改自己部门的客户
        Customer oldCustomer = customerMapper.selectCustomerById(customer.getId());
        if (!oldCustomer.getDeptId().equals(loginUser.getDeptId()))
        {
            throw new ServiceException("无权修改其他部门的客户信息");
        }

        // 设置更新信息
        customer.setUpdateBy(loginUser.getUsername());
        customer.setUpdateTime(new Date());

        return customerMapper.updateCustomer(customer);
    }

    @Override
    public List<Customer> selectCustomerList(Customer customer)
    {
        // 获取当前用户
        LoginUser loginUser = SecurityUtils.getLoginUser();

        // 如果不是管理员，添加部门过滤条件
        if (!SecurityUtils.isAdmin(loginUser.getUserId()))
        {
            customer.setDeptId(loginUser.getDeptId());
        }

        return customerMapper.selectCustomerList(customer);
    }
}
```

### 6.3 示例3：使用@DataScope注解实现数据权限

```java
@Service
public class CustomerServiceImpl implements ICustomerService
{
    @Autowired
    private CustomerMapper customerMapper;

    /**
     * 查询客户列表（自动根据用户权限过滤）
     * @DataScope注解会自动在SQL中添加部门权限条件
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<Customer> selectCustomerList(Customer customer)
    {
        // @DataScope会自动在SQL中添加类似以下条件：
        // AND (d.dept_id IN (SELECT dept_id FROM sys_role_dept WHERE role_id = 2))
        return customerMapper.selectCustomerList(customer);
    }
}
```

### 6.4 示例4：异步任务中获取用户（特殊处理）

```java
@Service
public class AsyncService
{
    /**
     * 异步任务中需要手动传递用户信息
     */
    public void asyncTask()
    {
        // 在主线程中获取用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long userId = loginUser.getUserId();
        String username = loginUser.getUsername();

        // 提交异步任务，手动传递用户信息
        AsyncManager.me().execute(() -> {
            // 异步线程中使用传递过来的用户信息
            System.out.println("异步任务执行，用户ID：" + userId);
            System.out.println("异步任务执行，用户名：" + username);

            // 注意：异步线程中无法直接使用SecurityUtils.getLoginUser()
            // 因为SecurityContextHolder是ThreadLocal的
        });
    }
}
```

---

## 🔒 7. 安全机制总结

### 7.1 安全特性

| 特性 | 说明 | 实现方式 |
|------|------|----------|
| **Token安全** | JWT Token只包含UUID，不含敏感信息 | JWT中只存储UUID和用户名 |
| **用户信息存储** | 完整用户信息存在Redis中，服务端可控 | Redis存储LoginUser对象 |
| **过期时间** | 30分钟（可配置） | Redis TTL + LoginUser.expireTime |
| **自动续期** | 不足20分钟自动续期 | JwtAuthenticationTokenFilter中实现 |
| **注销登录** | 删除Redis中的key即可 | `tokenService.delLoginUser(token)` |
| **强制下线** | 删除指定用户的Redis key | 删除 `login_tokens:uuid` |
| **多端登录** | 支持（每个设备有独立的UUID） | 每次登录生成新的UUID |
| **权限实时生效** | 每次请求都从Redis读取最新权限 | 过滤器每次从Redis获取LoginUser |
| **密码加密** | BCrypt加密存储 | `SecurityUtils.encryptPassword()` |
| **IP黑名单** | 支持配置IP黑名单 | 配置项：`sys.login.blackIPList` |

### 7.2 为什么不把用户信息直接放在JWT中？

**原因：**
1. **JWT一旦签发无法撤销**：将用户信息放在Redis中，可以随时让Token失效
2. **权限实时生效**：用户权限变更后，从Redis读取可以立即生效
3. **支持强制下线**：删除Redis key即可强制用户下线
4. **减少JWT体积**：JWT只存UUID，减少网络传输
5. **安全性更高**：敏感信息不在客户端存储

### 7.3 Token续期策略

**自动续期条件：**
- 距离过期时间不足20分钟时自动续期
- 续期后重新计算30分钟的有效期

**实现代码：** `TokenService.verifyToken()`

```java
// 如果距离过期不足20分钟，自动刷新
if (expireTime - currentTime <= 20 * 60 * 1000)
{
    refreshToken(loginUser);  // 重新设置过期时间
}
```

---

## ❓ 8. 常见问题与解答

### Q1: 如何手动刷新Token？

**接口方式：**
前端可以调用 `/getInfo` 接口，该接口会检查权限是否变更并自动刷新：

```java
@GetMapping("getInfo")
public AjaxResult getInfo()
{
    LoginUser loginUser = SecurityUtils.getLoginUser();
    Set<String> permissions = permissionService.getMenuPermission(loginUser.getUser());

    // 如果权限变更，刷新Token
    if (!loginUser.getPermissions().equals(permissions))
    {
        loginUser.setPermissions(permissions);
        tokenService.refreshToken(loginUser);
    }

    return AjaxResult.success();
}
```

**代码方式：**
```java
LoginUser loginUser = SecurityUtils.getLoginUser();
tokenService.refreshToken(loginUser);
```

### Q2: 如何强制用户下线？

```java
@Service
public class SysUserOnlineService
{
    @Autowired
    private TokenService tokenService;

    /**
     * 强制用户下线
     * @param token 用户的token（UUID）
     */
    public void forceLogout(String token)
    {
        // 删除Redis中的用户信息
        tokenService.delLoginUser(token);
    }
}
```

### Q3: 如何实现单点登录（一个用户只能一个设备登录）？

**实现思路：**
在用户登录时，检查该用户是否已经登录，如果已登录则删除旧Token。

**需要修改 `TokenService.createToken()` 方法：**

```java
public String createToken(LoginUser loginUser)
{
    // 先查询该用户是否已有登录Token
    String oldTokenKey = "login_user_" + loginUser.getUserId();
    String oldToken = redisCache.getCacheObject(oldTokenKey);

    // 如果存在旧Token，删除它
    if (StringUtils.isNotEmpty(oldToken))
    {
        delLoginUser(oldToken);
    }

    // 生成新Token
    String token = IdUtils.fastUUID();
    loginUser.setToken(token);
    setUserAgent(loginUser);
    refreshToken(loginUser);

    // 记录用户ID与Token的映射关系
    redisCache.setCacheObject(oldTokenKey, token, expireTime, TimeUnit.MINUTES);

    // ... 后续代码
}
```

### Q4: 如何修改Token有效期？

**配置文件修改：** `application.yml`

```yaml
token:
  # 修改为60分钟
  expireTime: 60
```

### Q5: 如何在拦截器或AOP中获取用户信息？

```java
@Aspect
@Component
public class DataScopeAspect
{
    @Before("@annotation(dataScope)")
    public void doBefore(JoinPoint point, DataScope dataScope)
    {
        // 获取当前用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();

        // 如果是管理员，不进行数据过滤
        if (user != null && !SecurityUtils.isAdmin(user.getUserId()))
        {
            // 添加数据权限过滤逻辑
            dataScopeFilter(point, user, dataScope);
        }
    }
}
```

### Q6: 前端如何存储和使用Token？

**存储：**
```javascript
// 登录成功后存储Token
localStorage.setItem('token', response.data.token);
```

**使用（Axios拦截器）：**
```javascript
// 请求拦截器
axios.interceptors.request.use(config => {
    // 从localStorage获取Token
    const token = localStorage.getItem('token');
    if (token) {
        // 添加到请求头
        config.headers['Authorization'] = 'Bearer ' + token;
    }
    return config;
});

// 响应拦截器（处理Token过期）
axios.interceptors.response.use(
    response => response,
    error => {
        if (error.response.status === 401) {
            // Token过期，跳转到登录页
            localStorage.removeItem('token');
            router.push('/login');
        }
        return Promise.reject(error);
    }
);
```

### Q7: 如何查看当前在线用户？

**接口：** `GET /system/monitor/online/list`

**实现原理：**
遍历Redis中所有 `login_tokens:*` 的key，获取所有在线用户。

```java
@Service
public class SysUserOnlineService
{
    @Autowired
    private RedisCache redisCache;

    public List<SysUserOnline> selectOnlineList()
    {
        List<SysUserOnline> userOnlineList = new ArrayList<>();

        // 获取所有在线用户的key
        Collection<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");

        for (String key : keys)
        {
            LoginUser loginUser = redisCache.getCacheObject(key);
            if (loginUser != null)
            {
                SysUserOnline userOnline = new SysUserOnline();
                userOnline.setTokenId(loginUser.getToken());
                userOnline.setUserName(loginUser.getUsername());
                userOnline.setIpaddr(loginUser.getIpaddr());
                userOnline.setLoginLocation(loginUser.getLoginLocation());
                userOnline.setBrowser(loginUser.getBrowser());
                userOnline.setOs(loginUser.getOs());
                userOnline.setLoginTime(loginUser.getLoginTime());

                userOnlineList.add(userOnline);
            }
        }

        return userOnlineList;
    }
}
```

### Q8: Token被盗用怎么办？

**安全建议：**
1. **使用HTTPS**：防止Token在传输过程中被窃取
2. **设置合理的过期时间**：30分钟是较为安全的选择
3. **记录登录IP**：检测异常登录（不同IP使用同一Token）
4. **支持强制下线**：发现异常立即强制用户下线
5. **敏感操作二次验证**：如修改密码、删除数据等需要输入密码

**异常检测示例：**
```java
public void verifyToken(LoginUser loginUser)
{
    // 检查当前IP是否与登录IP一致
    String currentIp = IpUtils.getIpAddr();
    if (!currentIp.equals(loginUser.getIpaddr()))
    {
        // 记录异常日志
        log.warn("Token疑似被盗用，登录IP：{}，当前IP：{}", loginUser.getIpaddr(), currentIp);

        // 可选：强制下线
        // throw new ServiceException("登录IP异常，请重新登录");
    }

    // 正常的Token续期逻辑
    if (expireTime - currentTime <= MILLIS_MINUTE_TWENTY)
    {
        refreshToken(loginUser);
    }
}
```

---

## 📚 9. 相关文件索引

### 核心类文件

| 类名 | 路径 | 说明 |
|------|------|------|
| `SecurityUtils` | `youkang-common/src/main/java/com/youkang/common/utils/SecurityUtils.java` | 安全工具类，获取用户信息 |
| `TokenService` | `youkang-framework/src/main/java/com/youkang/framework/web/service/TokenService.java` | Token服务，生成、验证、刷新 |
| `LoginUser` | `youkang-common/src/main/java/com/youkang/common/core/domain/model/LoginUser.java` | 登录用户模型 |
| `JwtAuthenticationTokenFilter` | `youkang-framework/src/main/java/com/youkang/framework/security/filter/JwtAuthenticationTokenFilter.java` | JWT认证过滤器 |
| `SysLoginService` | `youkang-framework/src/main/java/com/youkang/framework/web/service/SysLoginService.java` | 登录服务 |
| `SysLoginController` | `youkang-admin/src/main/java/com/youkang/web/controller/system/SysLoginController.java` | 登录控制器 |

### 配置文件

| 文件 | 路径 | 说明 |
|------|------|------|
| `application.yml` | `youkang-admin/src/main/resources/application.yml` | 主配置文件（Token配置） |
| `SecurityConfig` | `youkang-framework/src/main/java/com/youkang/framework/config/SecurityConfig.java` | Spring Security配置 |

---

## 🎓 10. 总结

**核心要点：**

1. **JWT + Redis 双重存储**：JWT存储UUID，Redis存储完整用户信息
2. **请求过滤器**：每个请求都会经过 `JwtAuthenticationTokenFilter` 处理
3. **自动续期**：不足20分钟自动刷新，提升用户体验
4. **SecurityUtils工具类**：统一的用户信息获取接口
5. **数据权限控制**：通过 `@DataScope` 注解实现行级数据权限

**开发建议：**

1. 在Controller或Service中获取用户信息，始终使用 `SecurityUtils` 工具类
2. 需要记录操作人时，使用 `SecurityUtils.getUsername()` 或 `SecurityUtils.getUserId()`
3. 实现数据权限时，优先考虑使用 `@DataScope` 注解
4. 敏感操作前，使用 `@PreAuthorize("@ss.hasPermi('...')")` 进行权限校验
5. 生产环境务必修改 `token.secret` 配置项

---

**文档版本：** v1.0
**最后更新：** 2025-11-20
**维护者：** 有康开发团队
