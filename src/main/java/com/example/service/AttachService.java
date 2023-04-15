package com.example.service;

import com.example.dto.attach.AttachResponseDTO;
import com.example.entity.AttachEntity;
import com.example.enums.Language;
import com.example.exception.attach.FileNotFoundException;
import com.example.exception.attach.FileUploadException;
import com.example.exception.attach.OriginalFileNameNullException;
import com.example.repository.AttachRepository;
import com.example.util.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;
@Service
public class AttachService {
    private final ResourceBundleService resourceBundleService;
    private final AttachRepository repository;


    @Value("${attach.upload.folder}")
    private String attachUploadFolder;


    @Autowired
    public AttachService(ResourceBundleService resourceBundleService,
                         AttachRepository attachRepository) {
        this.resourceBundleService = resourceBundleService;
        this.repository = attachRepository;

    }


    /**
     * This method is used for file uploading in DataBase
     * If File Name is Empty  ,throw FileNameNotFoundException()
     *
     * @param file MultipartHttpServletRequest
     * @return AttachDTO
     */


    public AttachResponseDTO uploadFile(MultipartFile file) {

        try {

            String pathFolder = getYmDString(); // 2022/04/23
            File folder = new File(attachUploadFolder + pathFolder); // attaches/2022/04/23

            if (!folder.exists()) folder.mkdirs();


            String fileName = UUID.randomUUID().toString(); // dasdasd-dasdasda-asdasda-asdasd
            String extension = getExtension(file.getOriginalFilename(), Language.UZ); //zari.jpg

            // attaches/2022/04/23/dasdasd-dasdasda-asdasda-asdasd.jpg
            byte[] bytes = file.getBytes();
            Path path = Paths.get(attachUploadFolder + pathFolder + "/" + fileName + "." + extension);
            Files.write(path, bytes);

            AttachEntity entity = new AttachEntity();
            entity.setId(fileName);
            entity.setOriginName(file.getOriginalFilename());
            entity.setType(extension);
            entity.setPath(pathFolder);
            entity.setSize(file.getSize());
            AttachEntity save = repository.save(entity);


            AttachResponseDTO dto = new AttachResponseDTO();
            dto.setId(entity.getId());
            dto.setSize(entity.getSize());
            dto.setType(entity.getType());
            dto.setPath(entity.getPath());
            dto.setOriginalName(entity.getOriginName());
            dto.setUrl(UrlUtil.url + fileName + "." + extension);
            return dto;
        } catch (IOException e) {
            throw new FileUploadException(resourceBundleService.getMessage("file.upload", Language.UZ));
        }

    }




    /**
     * This method is used for downloading file
     * If file is not exist DB, throw FileNotFoundException
     *
     * @param id Integer
     * @return Message
     */


    public Resource downloadFile(String id) {

        //we get the AttachEntity object from DB
        try {
            AttachEntity entity = getAttach(id);

            Path file = Paths.get(attachUploadFolder + entity.getPath() + "/" + entity.getId() + "." + entity.getType());

            return new UrlResource(file.toUri());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String deleteById(String fileName) {

        Optional<AttachEntity> optional = repository.findById(fileName);
        if (optional.isEmpty()) {
            throw new RuntimeException("Error");
        }

        try {
            AttachEntity entity = getAttach(fileName);
            Path file = Paths.get(attachUploadFolder + entity.getPath() + "/" + fileName + "." + entity.getType());

            Files.delete(file);

            return "deleted";
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }

    }


    private AttachEntity getAttach(String fileName) {
        String id = fileName.split("\\.")[0];
        Optional<AttachEntity> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw new FileNotFoundException("File Not Found");
        }
        return optional.get();
    }


    public String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);

        return year + "/" + month + "/" + day; // 2022/04/23
    }


    public String getExtension(String fileName, Language language) {
        // mp3/jpg/npg/mp4.....
        if (fileName == null) {
            throw new OriginalFileNameNullException(resourceBundleService.getMessage("file.name.null", language));
        }
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }


}
