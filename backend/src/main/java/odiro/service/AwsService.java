package odiro.service;

import lombok.extern.slf4j.Slf4j;
import odiro.exception.CustomException;
import odiro.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class AwsService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    public AwsService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String key = "avatars/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .acl("public-read")
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }

    public String uploadPhoto(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new CustomException(ErrorCode.FILE_IS_EMPTY, file);
        }
        // 로그 출력
        System.out.println("File Name: " + file.getOriginalFilename());
        System.out.println("File Size: " + file.getSize());
        System.out.println("Content Type: " + file.getContentType());
        try {
            String fileUrl = uploadFile(file);
            return fileUrl;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
