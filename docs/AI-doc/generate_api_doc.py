import re
import os
import sys
import glob
import json

# ==================== 1. Parse DTOs ====================
class_def = {}

def load_dto(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    classname = os.path.basename(filepath).replace('.java', '')
    extends_cls = None
    extends_match = re.search(r'public\s+class\s+\w+\s+(?:extends\s+(\w+))?', content)
    if extends_match and extends_match.group(1):
             extends_cls = extends_match.group(1)
    # Match schema
    pattern = r'@Schema\s*\(\s*description\s*=\s*"([^"]+)"\s*\)\s*(?:@[A-Za-z0-9_]+(?:\([^)]*\))?\s*)*private\s+(\S+)\s+(\w+);'
    matches = re.findall(pattern, content)
    fields = []
    for desc, field_type, field_name in matches:
        fields.append({"name": field_name, "type": field_type, "desc": desc})
    class_def[classname] = {"extends": extends_cls, "fields": fields}

def load_all_dtos():
    search_dirs = [
        "/root/www/youkang/youkang-backend/youkang-system/src/main/java/com/youkang/system/domain/req/",
        "/root/www/youkang/youkang-backend/youkang-system/src/main/java/com/youkang/system/domain/resp/"
    ]
    for d in search_dirs:
        for root, dirs, files in os.walk(d):
            for file in files:
                if file.endswith(".java"): load_dto(os.path.join(root, file))
    # Pre-define some known types
    class_def["PageReq"] = {
        "extends": None, "fields": [
            {"name": "pageNum", "type": "Integer", "desc": "页码 (默认 1)"},
            {"name": "pageSize", "type": "Integer", "desc": "每页数量 (默认 96)"}
        ]
    }

# ==================== 2. Mock Generator ====================
def generate_mock(classname, depth=0):
    if depth > 3: return "..."
    if classname not in class_def: return f"<{classname}>"
    c_def = class_def[classname]
    mock_dict = {}
    if c_def["extends"] and c_def["extends"] in class_def:
        ext_mock = generate_mock(c_def["extends"], depth+1)
        if isinstance(ext_mock, dict): mock_dict.update(ext_mock)
    for f in c_def["fields"]:
        name = f["name"]
        t = f["type"]
        if t in ["String"]: mock_dict[name] = "string"
        elif t in ["Integer", "Long", "int", "long"]: mock_dict[name] = 0
        elif t in ["Double", "Float"]: mock_dict[name] = 0.0
        elif t in ["Boolean", "boolean"]: mock_dict[name] = False
        elif t.startswith("List<"):
            inner = t[5:-1]
            mock_dict[name] = [generate_mock(inner, depth+1) if inner in class_def else f"<{inner}>"]
        elif t in class_def: mock_dict[name] = generate_mock(t, depth+1)
        else: mock_dict[name] = f"<{t}>"
    return mock_dict

def get_class_md_table(classname):
    if classname not in class_def: return ""
    c_def = class_def[classname]
    md = f"#### {classname}\n\n| 字段 | 类型 | 说明 |\n| :--- | :--- | :--- |\n"
    if c_def["extends"] and c_def["extends"] in class_def:
        ext = class_def[c_def["extends"]]
        for f in ext["fields"]: md += f"| {f['name']} | `{f['type']}` | (继承自 {c_def['extends']}) {f['desc']} |\n"
    for f in c_def["fields"]: md += f"| {f['name']} | `{f['type']}` | {f['desc']} |\n"
    md += "\n"
    return md

# ==================== 3. Controller Parser ====================
def generate_doc(controller_path):
    if not os.path.exists(controller_path):
        print(f"Error: {controller_path} not found")
        sys.exit(1)
        
    classname = os.path.basename(controller_path).replace('.java', '')
    prefix = classname.replace('Controller', '')

    with open(controller_path, "r", encoding="utf-8") as f:
        content = f.read()

    base_path_match = re.search(r'@RequestMapping\("([^"]+)"\)', content)
    base_path = base_path_match.group(1) if base_path_match else ""

    blocks = content.split("@Operation")
    methods = []

    for block in blocks[1:]:
        m_match = re.search(r'\(\s*summary\s*=\s*"([^"]+)"(?:\s*,\s*description\s*=\s*"([^"]+)")?\s*\)', block)
        if not m_match: continue
        summary = m_match.group(1)
        desc = m_match.group(2) or summary
        
        auth_match = re.search(r'@PreAuthorize\("([^"]+)"\)', block)
        auth = auth_match.group(1) if auth_match else ""
        
        http_match = re.search(r'@(PostMapping|GetMapping|PutMapping|DeleteMapping)\(?\s*(?:"([^"]+)"|value\s*=\s*"([^"]+)")?\s*\)?', block)
        if not http_match: continue
        http_method = http_match.group(1)
        sub_path = http_match.group(2) or http_match.group(3) or ""
        
        sig_match = re.search(r'public\s+R(?:<PageResp>|<([^>]+)>|[^A-Za-z\s]+)\s+(\w+)\s*\(', block)
        if not sig_match: continue
        r_type = sig_match.group(1) or ("PageResp" if "R<PageResp>" in sig_match.group(0) else "")
        m_name = sig_match.group(2)
        
        sig_start = sig_match.end() - 1
        count = 1
        i = sig_start + 1
        while i < len(block) and count > 0:
            if block[i] == '(': count += 1
            elif block[i] == ')': count -= 1
            i += 1
        params = block[sig_start+1:i-1]
        methods.append((summary, desc, auth, http_method, sub_path, r_type, m_name, params))

    md_out = f"""# {summary.split("管理")[0]} API 文档

**基础路径**: `{base_path}`
**说明**: {classname} 自动生成文档

## 目录
"""

    api_list = ""
    dto_used = set()

    for i, m in enumerate(methods):
        summary, desc, auth, http_method, sub_path, r_type, m_name, params = m
        full_path = f"{base_path}{sub_path}" if sub_path.startswith("/") else f"{base_path}/{sub_path}" if sub_path else base_path
        
        req_class = None
        if "@RequestBody" in params:
            req_match = re.search(r'@RequestBody\s+(\w+)', params)
            if req_match:
                req_class = req_match.group(1)
                dto_used.add(req_class)
                
        if r_type == "PageResp":
            r_type = f"{prefix}Resp"
                 
        if r_type and r_type not in ["Void", "String", "Integer"]: dto_used.add(r_type)

        md_out += f"- [{summary}](#{summary})\n"
        api_list += f"### {i+1}. {summary}\n"
        api_list += f"- **路径**: `{sub_path}` (完整: `{full_path}`)\n- **方法**: `{http_method.upper()}`\n- **说明**: {desc or summary}\n"
        if auth:
             auth_val = auth.split("'")[1] if "'" in auth else auth
             api_list += f"- **权限**: `{auth_val}`\n"
        if req_class: api_list += f"- **请求参数**: `[{req_class}](#{req_class.lower()})`\n"
        if r_type: api_list += f"- **响应结构**: `R<{r_type}>` (分页时外层为 PageResp，数据位于 data.rows)\n"
        
        if req_class and req_class in class_def:
             api_list += f"\n**请求示例 JSON**:\n```json\n{json.dumps(generate_mock(req_class), indent=2, ensure_ascii=False)}\n```\n"
        if r_type and r_type in class_def:
             mock_data = generate_mock(r_type)
             if r_type == "PageResp" or m_name == "list": mock_data = {"total": 100, "rows": [mock_data]}
             api_list += f"\n**响应数据结构示例 JSON (Data 节点内)**:\n```json\n{json.dumps(mock_data, indent=2, ensure_ascii=False)}\n```\n"
        api_list += "\n---\n\n"

    md_out += "\n---\n\n" + api_list
    md_out += "## 附录：数据结构 (DTO)\n\n"
    for dto in sorted(dto_used): md_out += get_class_md_table(dto)

    out_path = f"/root/www/youkang/youkang-backend/docs/AI-doc/{classname}.md"
    with open(out_path, "w", encoding="utf-8") as f:
        f.write(md_out)
    print(f"Generated {classname}.md successfully.")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python3 generate_api_doc.py <ControllerPath>")
        sys.exit(1)
    load_all_dtos()
    generate_doc(sys.argv[1])
