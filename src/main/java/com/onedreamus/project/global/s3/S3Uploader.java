package com.onedreamus.project.global.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucketname}")
    private String S3_BUCKET;

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

        // 확장자 획득 -> 옳바른 확장자가 없을 경우 예외 발생
        String extension = getFileExtension(filename)
                .orElseThrow(() -> new S3Exception(ErrorCode.INVALID_FILE_FORMAT));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(getContentType(extension));
        metadata.setContentLength(multipartFile.getSize());

        try {
            amazonS3Client.putObject(
                    new PutObjectRequest(S3_BUCKET, newFilename, multipartFile.getInputStream(), metadata));
        } catch (IOException e) {
            throw new S3Exception(ErrorCode.IMAGE_UPLOAD_FAIL);
        }

        return amazonS3Client.getUrl(S3_BUCKET, newFilename).toString();
    }

    /**
     * <p>S3 이미지 삭제</p>
     * @param fileName
     */
    public void deleteFile(String fileName) {
        try {
            amazonS3Client.deleteObject(S3_BUCKET, fileName);
            log.info(" S3 객체 삭제 : {}", fileName);
        } catch (SdkClientException e) {
            log.error("S3 객체 삭제 실패: {}", e.getMessage(), e);
            throw new S3Exception(ErrorCode.AWS_SDK_ERROR);
        }
    }

    /**
     * <p>S3 이미지 삭제</p>
     *
     * @param imageUrl
     */
    public void deleteImageByUrl(String imageUrl) {
        String fileName = getFileName(imageUrl);
        deleteFile(fileName);
    }

    private String createFileName(String fileName, ImageCategory imageCategory) {
        String random = UUID.randomUUID().toString();
        return imageCategory.getName() + "-" + random + fileName;
    }

    private String getContentType(String extension) {
        return "image/" + extension.toLowerCase();
    }

    private Optional<String> getFileExtension(String filename){
        if (filename == null || !filename.contains(".")) {
            return Optional.empty();
        }

        return Optional.of(filename.substring(filename.lastIndexOf(".") + 1));
    }

    /**
     * URL 에서 파일명 추출
     * @param imageUrl
     * @return
     */
    @NotNull
    public String getFileName(String imageUrl) {
        try{
            URI uri = new URI(imageUrl);
            String path = uri.getPath();
            return path.substring(path.lastIndexOf("/") + 1);
        } catch (URISyntaxException e) {
            log.error("잘못된 URL 형식: {}", imageUrl, e);
            throw new S3Exception(ErrorCode.INVALID_URL);
        }
    }
}
