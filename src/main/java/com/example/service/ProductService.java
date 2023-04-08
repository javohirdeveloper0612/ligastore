package com.example.service;

import com.example.dto.AttachResponseDTO;
import com.example.dto.CreateProductDto;
import com.example.dto.ResponseProductDto;
import com.example.entity.ProductEntity;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final AttachService attachService;

    @Autowired
    public ProductService(ProductRepository productRepository, AttachService attachService) {
        this.productRepository = productRepository;
        this.attachService = attachService;
    }


    /**
     * This method is used for adding new Product
     *
     * @param id
     * @param dto CreateProductDto
     * @return ResponseProductDto
     */
    public ResponseProductDto addProduct(Long id, CreateProductDto dto) {

        ProductEntity productEntity = getProductEntity(dto);
        ProductEntity savedProduct = productRepository.save(productEntity);
        return responseProductDto(savedProduct);
    }

    /**
     * This method is used for converting CreatedDto to ProductEntity
     *
     * @param dto CreateProductDto
     * @return ProductEntity
     */
    public ProductEntity getProductEntity(CreateProductDto dto) {
        AttachResponseDTO uploadFile = attachService.uploadFile(dto.getFile());
        ProductEntity product = new ProductEntity();
        product.setNameUz(dto.getName_uz());
        product.setNameRu(dto.getName_ru());
        product.setDescriptionUz(dto.getDescription_uz());
        product.setDescriptionRu(dto.getDescription_ru());
        product.setScore(dto.getScore());
        product.setAttachId(uploadFile.getId());
        return product;
    }

    /**
     * This method is used for converting ProductEntity to responseProductEntity
     *
     * @param productEntity ProductEntity
     * @return ResponseProductDto
     */
    public ResponseProductDto responseProductDto(ProductEntity productEntity) {
        ResponseProductDto dto = new ResponseProductDto();
        dto.setId(productEntity.getId());
        dto.setName_uz(productEntity.getNameUz());
        dto.setName_ru(productEntity.getNameRu());
        dto.setDescription_uz(productEntity.getDescriptionUz());
        dto.setDescription_ru(productEntity.getDescriptionRu());
        dto.setScore(productEntity.getScore());
        return dto;
    }
}
