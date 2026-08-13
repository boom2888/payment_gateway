//package com.games.s3;
//
//
//import com.amazonaws.AmazonServiceException;
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSCredentialsProvider;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.games.common.utils.DateUtils;
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//import java.net.URL;
//import java.time.LocalDateTime;
//
//
////String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
////
////
////        try {
////            String newFileName = isKeepName ? file.getOriginalFilename() : IdGenerator.generateKey(file.getOriginalFilename()).toString();
////            String keyName = category + "/" + (isKeepName ? file.getOriginalFilename() : (newFileName + "." + suffix));
////            File newFile = File.createTempFile(newFileName, "." + suffix);
////            file.transferTo(newFile);
////            URL url = AmazonS3Utils.uploadToS3Pub(newFile, keyName);
////            return CommonResponse.of(type?url.toString():"/attach"+url.getPath());
////        } catch (Exception e) {
////            info("上传文件失败[{}]", e.getMessage());
////            return CommonResponse.error("上传文件失败");
////        }
//
//@Component
//public class AmazonS3Utils {
//    S3Config config;
//
//
//    public URL uploadToS3(File file, String keyName) {
//        System.out.format("Uploading %s to S3 bucket %s...\n", keyName, config.getBucketName());
//        return uploadToS3Bucket(file, keyName, config.getBucketName(), true);
//    }
//
//    public URL uploadToS3Pub(File file, String /**/keyName) {
//        System.out.format("Uploading %s to S3 bucket %s...\n", keyName, keyName);
//        return uploadToS3Bucket(file, keyName, config.getBucketNamePub(), false);
//    }
//
//    private URL uploadToS3Bucket(File file, String keyName, String bucketName, Boolean isNeedSign) {
//        System.out.format("Uploading %s to S3 bucket %s...\n", keyName, bucketName);
//
//        try {
//            AWSCredentials awsCredentials = new BasicAWSCredentials(config.getAccessKey(), config.getSecretKey());
//            AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
//
//            AmazonS3 s3 = AmazonS3ClientBuilder.standard()
//                    .withCredentials(awsCredentialsProvider)
//                    .withRegion(config.getRegion())
//                    .build();
//
//            s3.putObject(bucketName, keyName, file);
//            if(isNeedSign) {
//                return s3.generatePresignedUrl(bucketName, keyName, DateUtils.fromJava8LocalDateToDate(LocalDateTime.now().plusHours(5L)));
//            } else  {
//                return s3.getUrl(bucketName, keyName);
//            }
//        } catch (AmazonServiceException e) {
//            throw new AmazonServiceException(e.getErrorMessage());
//        }
//    }
//
//    public URL getURLbyKeyName(String keyName) {
//
//        try {
//            AWSCredentials awsCredentials = new BasicAWSCredentials(config.getAccessKey(), config.getSecretKey());
//            AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
//
//            AmazonS3 s3 = AmazonS3ClientBuilder.standard()
//                    .withCredentials(awsCredentialsProvider)
//                    .withRegion(config.getRegion())
//                    .build();
//
//            return s3.generatePresignedUrl(config.getBucketName(), keyName, DateUtils.fromJava8LocalDateToDate(LocalDateTime.now().plusHours(5L)));
//
//        } catch (AmazonServiceException e) {
//            throw new AmazonServiceException(e.getErrorMessage());
//        }
//    }
//    private String getOrigin(String bucketName) {
//        return  config.getProtocol() + "//" + bucketName + ".s3." + config.getRegion() + "." + config.getHost() + "/";
//    }
//
//    public String getKeyName(String url) {
//        if (url.isEmpty()) return url;
//        String origin = getOrigin(config.getBucketName());
//        String signedKeyName = url.replace(origin, "");
//        if (signedKeyName.isEmpty()) return url;
//        return signedKeyName.split("\\?")[0];
//    }
//}
