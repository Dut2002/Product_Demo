package com.example.demo_oracle_db.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@RestController
@RequestMapping("api/file")
public class FileResourceController {

    //define a location
    public static final String DIRECTORY = System.getProperty("user.home") + "\\Downloads\\uploads\\";

    //Defile a method to upload
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("files") List<MultipartFile> fileList) throws IOException {
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : fileList) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            Path fileLocation = get(DIRECTORY, fileName).toAbsolutePath().normalize();
            copy(file.getInputStream(), fileLocation, REPLACE_EXISTING);
            fileNames.add(fileName);
        }
        return ResponseEntity.ok().body(fileNames);
    }

    @GetMapping("download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("filename") String fileName) throws IOException {
        Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(fileName);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException((fileName + "was not found on the server"));
        }
        Resource resource = new UrlResource(filePath.toUri());
        HttpHeaders headers = new HttpHeaders();
        headers.add("File-Name", fileName);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment:FILE-NAME=" + resource.getFilename());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath))).headers(headers).body(resource);
    }


}
