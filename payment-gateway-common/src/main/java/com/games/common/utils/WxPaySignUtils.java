package com.games.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.games.common.exception.BusinessException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.*;

public class WxPaySignUtils {

    public static void main(String[] args) {
        Map params = converResultStringToMap("app_name=app_dc_client&nonce=ibuaiVcKdpRxkhJA");
        String sigin = sign(params, "192006250b4c09247ec02edce69f6a2d", MD5);
        System.out.println(sigin);
    }


    public static final String MD5 = "MD5";
    public static final String HMAC_SHA256 = "HMAC-SHA256";

    public static String sign(Map data, String key){
        return sign(data, key, MD5);
    }

    public static Map<String, Object> getParams(Object data) {
        Map<String, Object> logicParam = BeanUtil.beanToMap(data, false, true);
        // 遍历所有 key-value 进行处理
        logicParam.replaceAll((k, v) -> (v instanceof Date) ? DateUtil.format((Date) v, "yyyy-MM-dd HH:mm:ss") : v);
        return logicParam;
    }
    
    /**
     * 签名
     * @param data
     * @param key
     * @param signType
     * @return
     */
    public static String sign(Map data, String key, String signType){
        if(null == data || data.size() == 0 || null == key || key.trim().equals("") || null == signType || !(MD5.equals(signType) || HMAC_SHA256.equals(signType))){
            throw new RuntimeException(MessageUtils.message("validation.parameter.missing"));
        }
        // 签名值
        String signKey = "";

        SortedMap sortedMap = new TreeMap<>(data);

        String paramStr = "";

        for (Object keyObj : sortedMap.entrySet()) {
            Map.Entry stringStringEntry = (Map.Entry)keyObj;
            if(paramStr.equals("")){
                paramStr = stringStringEntry.getKey() + "=" + stringStringEntry.getValue();
            }else{
                paramStr = paramStr + "&" +stringStringEntry.getKey() + "=" + stringStringEntry.getValue();
            }
        }

        paramStr = paramStr + "&key=" + key;
        if(signType.equals(MD5)){
            signKey = getMD5(paramStr);
        }else{
            signKey = getHmacSha256(paramStr,key);
        }
        String sign = signKey.toUpperCase();
        return sign;
    }

    /**

     * 获取MD5

     * @param str

     * @return

     */

    private static String getMD5(String str) {
        try {
// 生成一个MD5加密计算摘要

            MessageDigest md = MessageDigest.getInstance("MD5");

// 计算md5函数

            md.update(str.getBytes());

            return byteArrayToHexString(md.digest());

        } catch (Exception e) {
            throw BusinessException.api("md5.encryption.error");

        }

    }

    /**

     * 获取HmacSha256

     * @param message

     * @param key

     * @return

     */

    public static String getHmacSha256(String message,String key){
        String outPut= null;

        try{
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(),"HmacSHA256");

            sha256_HMAC.init(secret_key);

            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());

            outPut = byteArrayToHexString(bytes);

        }catch (Exception e){
            throw BusinessException.api("hmac.sha256.encryption.error");

        }

        return outPut;

    }

    /**

     * byte数组转16进制字符串

     * @param b

     * @return

     */

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                sb.append('0');
            sb.append(stmp);
        }

        return sb.toString().toLowerCase();

    }


    /**
     *
     * @param result 带分隔的url参数
     * @return
     */
    public static Map<String,String> converResultStringToMap(String result){
        Map<String,String> map =null;
        if(StringUtils.isNotBlank(result)){
            if(result.startsWith("{")&& result.endsWith("}")){
                result =result.substring(1,result.length ()-1);
            }
            map = parseQString(result);
        }
        return map;
    }
    private static Map<String, String> parseQString(String params) {
        Map<String,String> map=new HashMap<>();
        String[] pairs = params.split ("&");
        for(String item : pairs) {
            String[] pair  = item.split ("=");
            System.out.println (pair.length);
            if (pair .length==2){
                map.put (pair [0], pair [1]);
            }
        }
        return map ;
    }


}
