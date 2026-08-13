package com.games.common.utils.http;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.games.common.constant.Constants;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * 通用http发送方法
 *
 * @author lor
 */
public class HttpUtils
{
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param)
    {
        return sendGet(url, param, Constants.UTF8);
    }

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param contentType 编码类型
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, String contentType)
    {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try
        {
            String urlNameString = url + "?" + param;
            log.info("sendGet - {}", urlNameString);
            URL realUrl = UrlSecurityUtils.requirePublicHttpUrl(urlNameString);
            URLConnection connection = realUrl.openConnection();
            disableRedirects(connection);
            connection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), contentType));
            String line;
            while ((line = in.readLine()) != null)
            {
                result.append(line);
            }
        }
        catch (ConnectException e)
        {
            log.error("调用HttpUtils.sendGet ConnectException, url=" + url, e);
        }
        catch (SocketTimeoutException e)
        {
            log.error("调用HttpUtils.sendGet SocketTimeoutException, url=" + url, e);
        }
        catch (IOException e)
        {
            log.error("调用HttpUtils.sendGet IOException, url=" + url, e);
        }
        catch (Exception e)
        {
            log.error("调用HttpsUtil.sendGet Exception, url=" + url, e);
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (Exception ex)
            {
                log.error("调用in.close Exception, url=" + url, ex);
            }
        }
        return result.toString();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param)
    {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try
        {
            String urlNameString = url;
            log.info("sendPost - {}", urlNameString);
            URL realUrl = UrlSecurityUtils.requirePublicHttpUrl(urlNameString);
            URLConnection conn = realUrl.openConnection();
            disableRedirects(conn);
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null)
            {
                result.append(line);
            }
        }
        catch (ConnectException e)
        {
            log.error("调用HttpUtils.sendPost ConnectException, url=" + url, e);
        }
        catch (SocketTimeoutException e)
        {
            log.error("调用HttpUtils.sendPost SocketTimeoutException, url=" + url, e);
        }
        catch (IOException e)
        {
            log.error("调用HttpUtils.sendPost IOException, url=" + url, e);
        }
        catch (Exception e)
        {
            log.error("调用HttpsUtil.sendPost Exception, url=" + url, e);
        }
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ex)
            {
                log.error("调用in.close Exception, url=" + url, ex);
            }
        }
        return result.toString();
    }

    public static String sendSSLPost(String url, String param)
    {
        StringBuilder result = new StringBuilder();
        String urlNameString = url + "?" + param;
        try
        {
            log.info("sendSSLPost - {}", urlNameString);
            URL console = UrlSecurityUtils.requirePublicHttpUrl(urlNameString);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String ret = "";
            while ((ret = br.readLine()) != null)
            {
                if (ret != null && !"".equals(ret.trim()))
                {
                    result.append(new String(ret.getBytes("ISO-8859-1"), "utf-8"));
                }
            }
            log.info("recv - {}", result);
            conn.disconnect();
            br.close();
        }
        catch (ConnectException e)
        {
            log.error("调用HttpUtils.sendSSLPost ConnectException, url=" + url + ",param=" + param, e);
        }
        catch (SocketTimeoutException e)
        {
            log.error("调用HttpUtils.sendSSLPost SocketTimeoutException, url=" + url + ",param=" + param, e);
        }
        catch (IOException e)
        {
            log.error("调用HttpUtils.sendSSLPost IOException, url=" + url + ",param=" + param, e);
        }
        catch (Exception e)
        {
            log.error("调用HttpsUtil.sendSSLPost Exception, url=" + url + ",param=" + param, e);
        }
        return result.toString();
    }

        /**
     * 使用Hutool工具类发送GET请求
     *
     * @param url 请求URL
     * @param params 请求参数Map
     * @return 响应结果字符串
     */
        public static String sendGetWithMap(String url, Map<String, Object> params) {
            UrlSecurityUtils.requirePublicHttpUrl(url);
            try (HttpResponse response = HttpRequest.get(url)
                    .form(params)
                    .timeout(30000)
                    .execute()) {
                log.info("Sending GET request to {}", url);
                String result = response.body();

                return result;
            } catch (Exception e) {
                log.error("调用HttpUtils.sendGetWithMap异常, url={}", url, e);
                return "";
            }
        }

    /**
     * 使用OkHttp工具类发送GET请求
     *
     * @param url 请求URL
     * @param params 请求参数Map
     * @return 响应结果字符串
     */
    public static String sendGetWithOkHttp(String url, Map<String, Object> params) {
        HttpUrl.Builder httpBuilder = HttpUrl.get(url).newBuilder();
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                Object value = param.getValue();
                // 只有当 value 不为空时才添加参数
                if (value != null && !String.valueOf(value).trim().isEmpty()) {
                    httpBuilder.addQueryParameter(param.getKey(), String.valueOf(value));
                }
            }
        }

        String finalUrl = httpBuilder.build().toString();
        log.info("Sending GET request with OkHttp");

        return executeOkHttpGetRequest(finalUrl, "GET");
    }

    /**
     * 使用OkHttp工具类发送GET请求，返回指定类型的对象
     *
     * @param url 请求URL
     * @param params 请求参数Map
     * @param responseClass 响应对象类型
     * @param <T> 响应对象泛型
     * @return 响应结果对象
     */
    public static <T> T sendGetWithOkHttp(String url, Map<String, Object> params, Class<T> responseClass) {
        String response = sendGetWithOkHttp(url, params);
        return parseResponse(response, responseClass);
    }

    /**
     * 使用OkHttp工具类发送POST表单请求（application/x-www-form-urlencoded）
     *
     * @param url 请求URL
     * @param params 表单参数Map（仅在值非空时加入）
     * @return 响应结果字符串
     */
    public static String sendPostWithOkHttp(String url, Map<String, Object> params) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                Object value = entry.getValue();
                if (value != null && !String.valueOf(value).trim().isEmpty()) {
                    formBuilder.add(entry.getKey(), String.valueOf(value));
                }
            }
        }

        RequestBody body = formBuilder.build();
        log.info("Sending form POST request to {}", url);
        
        return executeOkHttpRequest(url, body, null, "POST form");
    }

    /**
     * 使用OkHttp工具类发送POST表单请求，返回指定类型的对象
     *
     * @param url 请求URL
     * @param params 表单参数Map（仅在值非空时加入）
     * @param responseClass 响应对象类型
     * @param <T> 响应对象泛型
     * @return 响应结果对象
     */
    public static <T> T sendPostWithOkHttp(String url, Map<String, Object> params, Class<T> responseClass) {
        String response = sendPostWithOkHttp(url, params);
        return parseResponse(response, responseClass);
    }

    /**
     * 使用OkHttp工具类发送POST JSON请求（application/json）
     *
     * @param url 请求URL
     * @param jsonBody JSON字符串
     * @param headers 可选请求头（如需要传递认证信息），允许为null
     * @return 响应结果字符串
     */
    public static String sendPostJsonWithOkHttp(String url, String jsonBody, Map<String, String> headers) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonBody == null ? "" : jsonBody, JSON);
        
        log.info("Sending JSON POST request to {}", url);
        
        return executeOkHttpRequest(url, body, headers, "POST JSON");
    }

    /**
     * 使用OkHttp工具类发送POST JSON请求，返回指定类型的对象
     *
     * @param url 请求URL
     * @param jsonBody JSON字符串
     * @param headers 可选请求头（如需要传递认证信息），允许为null
     * @param responseClass 响应对象类型
     * @param <T> 响应对象泛型
     * @return 响应结果对象
     */
    public static <T> T sendPostJsonWithOkHttp(String url, String jsonBody, Map<String, String> headers, Class<T> responseClass) {
        String response = sendPostJsonWithOkHttp(url, jsonBody, headers);
        return parseResponse(response, responseClass);
    }

    /**
     * 使用OkHttp工具类发送POST JSON请求（application/json）
     *
     * @param url 请求URL
     * @param object 请求对象，将自动转换为JSON字符串
     * @param headers 可选请求头（如需要传递认证信息），允许为null
     * @return 响应结果字符串
     */
    public static String sendPostJsonWithOkHttp(String url, Object object, Map<String, String> headers) {
        // 将对象转换为JSON字符串
        String jsonBody = "";
        if (object != null) {
            try {
                // 使用Jackson ObjectMapper将对象转换为JSON字符串
                ObjectMapper objectMapper = new ObjectMapper();
                jsonBody = objectMapper.writeValueAsString(object);
            } catch (Exception e) {
                log.error("对象转换为JSON失败: {}", e.getMessage());
                return "";
            }
        }
        
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonBody, JSON);
        
        log.info("Sending JSON POST request to {}", url);
        
        return executeOkHttpRequest(url, body, headers, "POST JSON");
    }

    /**
     * 使用OkHttp工具类发送POST JSON请求，返回指定类型的对象
     *
     * @param url 请求URL
     * @param object 请求对象，将自动转换为JSON字符串
     * @param headers 可选请求头（如需要传递认证信息），允许为null
     * @param responseClass 响应对象类型
     * @param <T> 响应对象泛型
     * @return 响应结果对象
     */
    public static <T> T sendPostJsonWithOkHttp(String url, Object object, Map<String, String> headers, Class<T> responseClass) {
        String response = sendPostJsonWithOkHttp(url, object, headers);
        return parseResponse(response, responseClass);
    }

    /**
     * 使用OkHttp工具类发送DELETE请求
     *
     * @param url 请求URL
     * @param headers 可选请求头（如需要传递认证信息），允许为null
     * @return 响应结果字符串
     */
    public static String sendDeleteWithOkHttp(String url, Map<String, String> headers) {
        log.info("Sending DELETE request to {}", url);

        Request.Builder builder = new Request.Builder()
                .url(url)
                .delete();

        if (headers != null && !headers.isEmpty()) {
            builder.headers(Headers.of(headers));
        }

        Request request = builder.build();
        return executeOkHttpRequestInternal(request, "DELETE");
    }

    /**
     * 解析响应JSON字符串为指定类型的对象
     *
     * @param response JSON响应字符串
     * @param responseClass 目标对象类型
     * @param <T> 目标对象泛型
     * @return 解析后的对象，解析失败时返回null
     */
    private static <T> T parseResponse(String response, Class<T> responseClass) {
        if (response == null || response.trim().isEmpty()) {
            log.warn("响应内容为空，无法解析为对象");
            return null;
        }
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response, responseClass);
        } catch (Exception e) {
            log.error("JSON响应解析失败: {}, response: {}", e.getMessage(), response);
            return null;
        }
    }

    /**
     * 通用方法：执行OkHttp GET请求的通用逻辑
     *
     * @param url 请求URL
     * @param requestType 请求类型（用于日志）
     * @return 响应结果字符串
     */
    private static String executeOkHttpGetRequest(String url, String requestType) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        return executeOkHttpRequestInternal(request, requestType);
    }

    /**
     * 通用方法：执行OkHttp POST请求的通用逻辑
     *
     * @param url 请求URL
     * @param body 请求体
     * @param headers 请求头
     * @param requestType 请求类型（用于日志）
     * @return 响应结果字符串
     */
    private static String executeOkHttpRequest(String url, RequestBody body, Map<String, String> headers, String requestType) {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);

        if (headers != null && !headers.isEmpty()) {
            builder.headers(Headers.of(headers));
        }

        Request request = builder.build();
        return executeOkHttpRequestInternal(request, requestType);
    }

    /**
     * 最底层通用方法：执行OkHttp请求的核心逻辑
     *
     * @param request 构建好的请求对象
     * @param requestType 请求类型（用于日志）
     * @return 响应结果字符串
     */
    private static String executeOkHttpRequestInternal(Request request, String requestType) {
        UrlSecurityUtils.requirePublicHttpUrl(request.url().toString());
        OkHttpClient client = new OkHttpClient.Builder()
                .followRedirects(false)
                .followSslRedirects(false)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("OkHttp {} request failed with code: {}", requestType, response.code());
                return "";
            }
            String responseBody = response.body() != null ? response.body().string() : "";
            log.info("recv with OkHttp - {}", responseBody);
            return responseBody;
        } catch (IOException e) {
            log.error("调用HttpUtils.executeOkHttpRequestInternal with Exception, url={}, requestType={}", 
                    request.url(), requestType, e);
            return "";
        }
    }

    private static void disableRedirects(URLConnection connection)
    {
        if (connection instanceof HttpURLConnection)
        {
            ((HttpURLConnection) connection).setInstanceFollowRedirects(false);
        }
    }

}
