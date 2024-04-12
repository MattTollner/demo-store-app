package com.mattcom.demostoreapp.s3;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", allowCredentials = "false", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.DELETE})
public class AdminController {

    private final S3Service s3Service;

    public AdminController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/gets3")
    public String getS3() {
        return s3Service.generateUploadUrl();
    }


}
