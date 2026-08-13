package com.games.web.controller;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.JSONObject;

public class Nuvei3DSParser {

    public static void main(String[] args) {
        // 示例回调中的 cres 参数值
        String cres = "eyJ0aHJlZURTU2VydmVyVHJhbnNJRCI6IjhmODg4ZjA4LWQ2ZmItNGIzMy04MmY3LTUyOWZiOGU5YzQzOCIsImFjc1RyYW5zSUQiOiJiZTA4MDY1ZC04M2Q1LTQzMzQtOTVlMS1mMGFmMTNmNzAwMzUiLCJtZXNzYWdlVHlwZSI6IkNSZXMiLCJtZXNzYWdlVmVyc2lvbiI6IjIuMi4wIiwidHJhbnNTdGF0dXMiOiJZIiwibWVzc2FnZUV4dGVuc2lvbiI6W3sibmFtZSI6Im1zZ2V4dG5hbWUiLCJpZCI6IjUwMTM0MTU5MkJfMDAwMV80NTY4IiwiY3JpdGljYWxpdHlJbmRpY2F0b3IiOmZhbHNlLCJkYXRhIjp7InZhbHVlT25lIjoibWVzc2FnZWV4dGVuc2lvbmRhdGEiLCJ2YWx1ZVR3byI6Im1vcmVtZXNzYWdlZXh0ZW5zaW9uZGF0YSJ9fV0sImFjc1NpZ25lZENvbnRlbnQiOm51bGx9";

        // 1. Base64 解码
        byte[] decodedBytes = Base64Decoder.decode(cres);
        String jsonStr = new String(decodedBytes);

        // 2. 解析为 JSON 对象
        JSONObject json = JSONUtil.parseObj(jsonStr);

        // 3. 打印结果
        System.out.println("=== 解析后的 JSON ===");
        System.out.println(JSONUtil.toJsonPrettyStr(json));

        // 4. 可按需取值，例如：
        String transStatus = json.getStr("transStatus");
        System.out.println("交易状态 transStatus: " + transStatus);
    }
}
