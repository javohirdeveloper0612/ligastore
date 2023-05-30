package com.example.service;

import com.example.dto.attach.AttachResponseDTO;
import com.example.entity.AttachEntity;
import com.example.enums.Language;
import com.example.exception.attach.FileNotFoundException;
import com.example.exception.attach.FileUploadException;
import com.example.exception.attach.OriginalFileNameNullException;
import com.example.repository.AttachRepository;
import com.example.util.UrlUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
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
            String pathFolder = getYmDString();
            File folder = new File(attachUploadFolder + pathFolder);
            if (!folder.exists()) folder.mkdirs();

            String fileName = UUID.randomUUID().toString();
            String extension = getExtension(file.getOriginalFilename(), Language.UZ); //zari.jpg

            byte[] bytes = file.getBytes();
            Path path = Paths.get(attachUploadFolder + pathFolder + "/" + fileName + "." + extension);
            Files.write(path, bytes);
            var entity = repository.save(getAttach(file, fileName, extension, pathFolder));
            return getAttachDTO(entity, fileName, extension);
        } catch (IOException e) {
            throw new FileUploadException(resourceBundleService.getMessage("file.upload", Language.UZ));
        }

    }

    public AttachEntity getAttach(MultipartFile file, String fileName, String extension, String pathFolder) {
        var entity = new AttachEntity();
        entity.setId(fileName);
        entity.setOriginName(file.getOriginalFilename());
        entity.setType(extension);
        entity.setPath(pathFolder);
        entity.setSize(file.getSize());
        return entity;
    }

    /**
     * This method is used for converting AttachEntity to DTO
     *
     * @param entity    AttachEntity
     * @param fileName  String
     * @param extension String
     * @return AttachResponseDTO
     */
    public AttachResponseDTO getAttachDTO(AttachEntity entity, String fileName, String extension) {
        var dto = new AttachResponseDTO();
        dto.setId(entity.getId());
        dto.setSize(entity.getSize());
        dto.setType(entity.getType());
        dto.setPath(entity.getPath());
        dto.setOriginalName(entity.getOriginName());
        dto.setUrl(UrlUtil.url + fileName + "." + extension);
        return dto;
    }

    /**
     * This method is used for downloading file
     * If file is not exist DB, throw FileNotFoundException
     *
     * @param id Integer
     * @return Message
     */
    public Resource downloadFile(String id) {
        try {
            var entity = getAttach(id);
            var file = Paths.get(attachUploadFolder + entity.getPath() + "/" + entity.getId() + "." +
                    entity.getType());
            return new UrlResource(file.toUri());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used for
     *
     * @param fileName String
     * @return String
     */
    public void deleteById(String fileName) {

        var optional = repository.findById(fileName);
        if (optional.isEmpty()) throw new FileNotFoundException(resourceBundleService.getMessage
                ("file.not.found", Language.UZ));

        try {
            var entity = getAttach(fileName);
            var file = Paths.get(attachUploadFolder + entity.getPath() + "/" + fileName + "." + entity.getType());
            Files.delete(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public AttachEntity getAttach(String fileName) {
        var id = fileName.split("\\.")[0];
        var optional = repository.findById(id);
        if (optional.isEmpty()) throw new FileNotFoundException("File Not Found");
        return optional.get();
    }


    public String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);
        return year + "/" + month + "/" + day; // 2022/04/23
    }


    public String getExtension(String fileName, Language language) {
        if (Objects.isNull(fileName))
            throw new OriginalFileNameNullException(resourceBundleService.getMessage("file.name.null", language));
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    public String updateById(String attachId, AttachResponseDTO dto) {
        var optional = repository.findById(attachId);
        if (optional.isEmpty())
            throw new FileNotFoundException(resourceBundleService.getMessage("file.not.found", Language.UZ));
        try {
            var entity = getAttach(attachId);
            Path file = Paths.get(attachUploadFolder + entity.getPath() + "/" + attachId + "." + entity.getType());
            Files.delete(file);
            entity.setId(dto.getId());
            entity.setPath(dto.getPath());
            entity.setSize(dto.getSize());
            entity.setOriginName(dto.getOriginalName());
            entity.setType(dto.getType());
            repository.save(entity);
            return "Updated";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
