package odiro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import odiro.service.AwsService;

import java.io.IOException;

@RestController
@RequestMapping("/profile")
public class AwsController {

    private final AwsService awsService;

    public AwsController(AwsService awsService) {
        this.awsService = awsService;
    }

    @PostMapping("profile/upload-photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file) throws IOException {
        String fileUrl = awsService.uploadPhoto(file);
        return ResponseEntity.ok(fileUrl);
    }

}

