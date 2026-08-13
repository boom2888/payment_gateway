package com.games.web.controller.common;

import com.games.common.config.RuoYiConfig;
import com.games.common.constant.Constants;
import com.games.common.core.domain.AjaxResult;
import com.games.common.core.domain.R;
import com.games.common.utils.StringUtils;
import com.games.common.utils.file.FileUploadUtils;
import com.games.common.utils.file.FileUtils;
import com.games.framework.config.ServerConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 通用请求处理
 * 
 * @author lor
 */
@Api("通用接口")
@RestController
public class CommonController
{
    private static final long MAX_REMOTE_DOWNLOAD_SIZE = 20L * 1024 * 1024;
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;

//    @Autowired
//    private CreditService creditService;
//
//    @Autowired
//    private CacheService cacheService;

    @ApiOperation("健康检查")
    @GetMapping("/common/safeCheck")
    @ApiResponse(code = 200, message = "健康检查", response = R.class)
    public R<String> safeCheck() {
        return R.ok();
    }

    /**
     * 通用下载请求
     * 
     * @param fileName 文件名称
     * @param delete 是否删除
     */
    @GetMapping("common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.checkAllowDownload(fileName))
            {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = RuoYiConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("X-Content-Type-Options", "nosniff");
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFile(filePath);
            }
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求
     */
    @ApiOperation("通用上传请求")
    @PostMapping("uploadFile")
    public AjaxResult uploadFile(MultipartFile file) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = new FileUploadUtils().upload(filePath, file).realPath;
            String url = RuoYiConfig.getFilehost() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

//    @ApiOperation("上传图片")
//    @PostMapping("/uploadImage")
//    public R<FileDto> uploadImage(MultipartFile file)
//    {
//        if(file == null)throw new BusinessException("USER_AVATAR_NULL_ERR");
//        FileDto dto = creditService.uploadImage(file);
//        cacheService.getCacheImageMap().put(dto.getCode(), dto.getUrl());
//        return R.okData(dto);
//    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/common/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
            if (!FileUtils.checkAllowDownload(resource))
            {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = RuoYiConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通过HTTP URL下载文件
     *
     * @param fileUrl 完整的文件URL地址
     */
    @GetMapping("common/downloadUrl")
    public ResponseEntity<byte[]> downloadUrl(String fileUrl) {
        HttpURLConnection conn = null;
        try {
            if (!FileUtils.checkAllowDownload(fileUrl)) {
                throw new Exception(StringUtils.format("文件地址({})非法，不允许下载。 ", fileUrl));
            }

            URL url = validatePublicHttpUrl(fileUrl);

            // 获取文件名
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            String realFileName = System.currentTimeMillis() + "_" + fileName;

            // 创建URL连接
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(10 * 1000);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");

            // 获取输入流
            int status = conn.getResponseCode();
            if (status < 200 || status >= 300) {
                throw new Exception("Remote server returned HTTP " + status);
            }
            long contentLength = conn.getContentLengthLong();
            if (contentLength > MAX_REMOTE_DOWNLOAD_SIZE) {
                throw new Exception("Remote file is too large");
            }
            byte[] content;
            try (InputStream inputStream = conn.getInputStream()) {
                content = inputStream.readNBytes((int) MAX_REMOTE_DOWNLOAD_SIZE + 1);
            }
            if (content.length > MAX_REMOTE_DOWNLOAD_SIZE) {
                throw new Exception("Remote file is too large");
            }

            String disposition = ContentDisposition.attachment()
                    .filename(realFileName, StandardCharsets.UTF_8)
                    .build()
                    .toString();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
                    .header("X-Content-Type-Options", "nosniff")
                    .body(content);
        } catch (Exception e) {
            log.error("下载文件失败", e);
            return ResponseEntity.internalServerError().build();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private URL validatePublicHttpUrl(String fileUrl) throws Exception {
        URL url = new URL(fileUrl);
        if (!("http".equalsIgnoreCase(url.getProtocol()) || "https".equalsIgnoreCase(url.getProtocol()))) {
            throw new Exception("Only HTTP(S) URLs are allowed");
        }
        if (StringUtils.isEmpty(url.getHost()) || StringUtils.isNotEmpty(url.getUserInfo())) {
            throw new Exception("Invalid remote URL");
        }
        for (InetAddress address : InetAddress.getAllByName(url.getHost())) {
            if (address.isAnyLocalAddress() || address.isLoopbackAddress() || address.isLinkLocalAddress()
                    || address.isSiteLocalAddress() || address.isMulticastAddress()) {
                throw new Exception("Private network addresses are not allowed");
            }
        }
        return url;
    }
}
