package odiro.controller;

import io.swagger.v3.oas.annotations.Parameter; // Swagger 관련 애너테이션
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import odiro.service.AwsService;

@RestController
@RequestMapping("/profile")
public class AwsController {

    private final AwsService awsService;

    public AwsController(AwsService awsService) {
        this.awsService = awsService;
    }

    @PostMapping("profile/upload-photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded!");
        }
        // 로그 출력
        System.out.println("File Name: " + file.getOriginalFilename());
        System.out.println("File Size: " + file.getSize());
        System.out.println("Content Type: " + file.getContentType());
        try {
            String fileUrl = awsService.uploadFile(file);
            return ResponseEntity.ok("File uploaded successfully: " + fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("File upload failed!");
        }
    }

}

