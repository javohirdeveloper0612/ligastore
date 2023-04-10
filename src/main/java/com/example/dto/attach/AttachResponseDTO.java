package com.example.dto.attach;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class AttachResponseDTO {
    private String id;
    private String originalName;
    private String path;
    private Long size;
    private String type;
    private LocalDateTime createdData;
    private String url;

}