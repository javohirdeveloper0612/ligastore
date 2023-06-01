package com.example.controller;

import com.example.service.AttachService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequestMapping("/api/attach")
public class AttachController {
    private final AttachService attachService;

    @Autowired
    public AttachController(AttachService attachService) {
        this.attachService = attachService;
    }


    @PostMapping(value = "/public/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@ModelAttribute("file") MultipartFile file) {
        log.info("upload file : multipartFile {} ", file);
        return ResponseEntity.ok(attachService.uploadFile(file));
    }


    @GetMapping(value = "/public/download/{id}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<?> downloadFile(@PathVariable(name = "id") String id) {
        var file = attachService.downloadFile(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);

    }


}
