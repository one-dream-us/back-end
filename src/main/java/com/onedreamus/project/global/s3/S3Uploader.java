package com.onedreamus.project.global.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.thisismoney.exception.S3Exception;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucketname}")
    private String bucket;

    /**
     * <p>S3 이미지 업로드</p>
     * S3에 이미지 업로드 후, 이미지 URL 반환
     * @param multipartFile
     * @param imageCategory
     * @return
     */
    public String uploadMultipartFileByStream(MultipartFile multipartFile, ImageCategory imageCategory) {

        String filename = multipartFile.getOriginalFilename();
        String newFilename = createFileName(filename, imageCategory);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(getContentType(filename));
        metadata.setContentLength(multipartFile.getSize());

        try {
            amazonS3Client.putObject(
                    new PutObjectRequest(bucket, newFilename, multipartFile.getInputStream(), metadata));
        } catch (IOException e) {
            throw new S3Exception(ErrorCode.IMAGE_UPLOAD_FAIL);
        }

        return amazonS3Client.getUrl(bucket, newFilename).toString();
    }

    /**
     * <p>S3 이미지 삭제</p>
     * @param fileName
     */
    public void deleteFile(String fileName) {
        try {
            amazonS3Client.deleteObject(bucket, fileName);
            log.info(" S3 객체 삭제 : {}", fileName);
        } catch (SdkClientException e) {
            throw new S3Exception(ErrorCode.AWS_SDK_ERROR);
        }
    }

    private String createFileName(String fileName, ImageCategory imageCategory) {
        String random = UUID.randomUUID().toString();
        return imageCategory.getName() + "-" + random + fileName;
    }

    private String getContentType(String filename) {
        int idx = filename.lastIndexOf(".");
        String extension = filename.substring(idx + 1);
        return "image/" + extension;
    }

    private void deleteUploadedImageList(List<String> imageUrlList) {
        for (String imageUrl : imageUrlList) {
            deleteFile(getFileName(imageUrl));
        }
    }

    @NotNull
    public String getFileName(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }
}
