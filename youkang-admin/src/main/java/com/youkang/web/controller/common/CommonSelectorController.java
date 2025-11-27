package com.youkang.web.controller.common;

import com.youkang.common.core.controller.BaseController;
import com.youkang.common.core.domain.AjaxResult;
import com.youkang.common.core.domain.SelectorDTO;
import com.youkang.common.core.domain.SelectorQueryReq;
import com.youkang.framework.web.service.selector.ICommonSelector;
import com.youkang.framework.web.service.selector.SelectorFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通用选择器控制器
 * 提供统一的下拉框数据接口
 *
 * @author youkang
 */
@Tag(name = "通用选择器", description = "提供统一的下拉框数据接口")
@RestController
@RequestMapping("/common/selector")
public class CommonSelectorController extends BaseController {

    @Autowired
    private SelectorFactory selectorFactory;

    /**
     * 获取选择器数据列表
     *
     * @param type     选择器类型 (user/dept/role/post/dict)
     * @param keyword  模糊查询关键字（可选）
     * @param param    额外参数（可选，如字典类型需要传入dictType）
     * @param pageSize 分页大小（默认20）
     * @return 选择器数据列表
     */
    @Operation(summary = "获取选择器数据列表", description = "根据类型和关键字查询下拉框数据，支持模糊查询")
    @GetMapping("/list")
    public AjaxResult list(
            @Parameter(description = "选择器类型 (user/dept/role/post/dict)", required = true)
            @RequestParam String type,
            @Parameter(description = "模糊查询关键字")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "额外参数（如字典类型需要传入dictType）")
            @RequestParam(required = false) String param,
            @Parameter(description = "分页大小（默认20）")
            @RequestParam(required = false, defaultValue = "20") Integer pageSize) {

        SelectorQueryReq queryReq = new SelectorQueryReq();
        queryReq.setType(type);
        queryReq.setKeyword(keyword);
        queryReq.setParam(param);
        queryReq.setPageSize(pageSize);

        ICommonSelector selector = selectorFactory.getSelector(type);
        List<SelectorDTO> result = selector.getSelectorList(queryReq);

        return success(result);
    }

    /**
     * POST方式获取选择器数据列表
     *
     * @param queryReq 查询请求对象
     * @return 选择器数据列表
     */
    @Operation(summary = "获取选择器数据列表(POST)", description = "根据类型和关键字查询下拉框数据，支持模糊查询")
    @PostMapping("/list")
    public AjaxResult listByPost(@RequestBody SelectorQueryReq queryReq) {
        ICommonSelector selector = selectorFactory.getSelector(queryReq.getType());
        List<SelectorDTO> result = selector.getSelectorList(queryReq);

        return success(result);
    }

    /**
     * 获取所有支持的选择器类型
     *
     * @return 选择器类型列表
     */
    @Operation(summary = "获取支持的选择器类型", description = "返回所有可用的选择器类型列表")
    @GetMapping("/types")
    public AjaxResult getSupportedTypes() {
        List<String> types = selectorFactory.getSupportedTypes();
        return success(types);
    }
}
