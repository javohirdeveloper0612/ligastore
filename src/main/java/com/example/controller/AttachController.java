package com.example.controller;
import com.example.dto.attach.AttachResponseDTO;
import com.example.service.AttachService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Tag(name = "Attach Controller", description = "This controller for file uploading and file downloading")
@RestController
@RequestMapping("/api/attach")
public class AttachController {
    private final AttachService attachService;

    @Autowired
    public AttachController(AttachService attachService) {
        this.attachService = attachService;
    }


    /**
     * This method is used for file uploading in DataBase
     * If File Name is Empty  ,throw FileNameNotFoundException()
     *
     * @param file MultipartHttpServletRequest
     * @return AttachDTO
     */
    @PostMapping(value = "/public/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload File API", description = "This method uploads the file in DataBase")
    public ResponseEntity<?> uploadFile(@ModelAttribute("file") MultipartFile file) {
        log.info("upload file : multipartFile {} ", file);
        AttachResponseDTO result = attachService.uploadFile(file);
        return ResponseEntity.ok(result);
    }


    /**
     * This method is used for downloading file
     * If file is not exist DB, throw FileNotFoundException
     *
     * @param id       Integer
     * @return Message
     */
    @GetMapping(value = "/public/download/{id}",produces = MediaType.ALL_VALUE)
    @Operation(summary = "Download method", description = "This method used for downloading file")
    public ResponseEntity<?> downloadFile(@PathVariable String id) {

        Resource file = attachService.downloadFile(id);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);

    }




}
