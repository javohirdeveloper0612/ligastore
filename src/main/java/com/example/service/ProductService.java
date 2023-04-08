package com.example.service;

import com.example.dto.AttachResponseDTO;
import com.example.dto.ProductDto;
import com.example.dto.ResponseProductDto;
import com.example.entity.CategoryEntity;
import com.example.entity.ProductEntity;
import com.example.enums.Language;
import com.example.exception.EmptyListException;
import com.example.exception.NotFoundParentCategory;
import com.example.exception.ProductNotFoundException;
import com.example.repository.CategoryRepository;
import com.example.repository.ProductRepository;
import com.example.util.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final AttachService attachService;
    private final CategoryRepository categoryRepository;
    private final ResourceBundleService resourceBundleService;

    @Autowired
    public ProductService(ProductRepository productRepository, AttachService attachService,
                          CategoryRepository categoryRepository, ResourceBundleService resourceBundleService) {
        this.productRepository = productRepository;
        this.attachService = attachService;
        this.categoryRepository = categoryRepository;
        this.resourceBundleService = resourceBundleService;
    }


    /**
     * This method is used for adding new Product
     *
     * @param categoryId Long
     * @param dto        ProductDto
     * @param language   language
     * @return ResponseProductDto
     */
    public ResponseProductDto addProduct(Long categoryId, ProductDto dto, Language language) {
        Optional<CategoryEntity> optional = categoryRepository.findById(categoryId);
        if (optional.isEmpty()) {
            throw new NotFoundParentCategory(resourceBundleService.getMessage("parent.not.found", language));
        }
        ProductEntity productEntity = getProductEntity(dto, optional.get());
        ProductEntity savedProduct = productRepository.save(productEntity);
        return responseProductDto(savedProduct);
    }


    /**
     * This method is used for editing product data if product_id not founded
     * throw new ProductNotFoundException
     *
     * @param productDto ProductDto
     * @param language   Language
     * @return ResponseProductDto
     */
    public ResponseProductDto editeProduct(Long productId, ProductDto productDto, Language language) {

        Optional<ProductEntity> optional = productRepository.findById(productId);
        if (optional.isEmpty()) {
            throw new ProductNotFoundException(resourceBundleService.getMessage("product.not.found", language));
        }

        ProductEntity productEntity = optional.get();
        ProductEntity product = getProductEntity(productDto, productEntity);
        ProductEntity editedProduct = productRepository.save(product);
        return responseProductDto(editedProduct);

    }


    /**
     * This method is used for viewing all the product data list if product list is empty
     * throw EmptyListException
     *
     * @param category_id Long
     * @param language    Language
     * @return List<ResponseProductDto></>
     */
    public List<ResponseProductDto> getProductList(Long category_id, Language language) {
        Optional<CategoryEntity> optional = categoryRepository.findById(category_id);
        if (optional.isEmpty()) {
            throw new NotFoundParentCategory(resourceBundleService.getMessage("category.not.found", language));
        }

        List<ProductEntity> list = productRepository.findAllByCategoryId(category_id);
        if (list.isEmpty()) {
            throw new EmptyListException(resourceBundleService.getMessage("empty.list.product", language));
        }
        return getProductEntityList(list, language);
    }


    /**
     * This method is used for getting ProductEntity List
     *
     * @param list     ProductEntity
     * @param language Language
     * @return List<ResponseProductEntity></>
     */
    public List<ResponseProductDto> getProductEntityList(List<ProductEntity> list, Language language) {
        List<ResponseProductDto> productDtoList = new ArrayList<>();
        for (ProductEntity product : list) {
            productDtoList.add(responseProductDtoByLan(product, language));
        }
        return productDtoList;
    }

    /**
     * This method is used for converting CreatedDto to ProductEntity
     * If Category not Found throw new NotFoundParentCategoryException
     *
     * @param dto ProductDto
     * @return ProductEntity
     */
    public ProductEntity getProductEntity(ProductDto dto, ProductEntity product) {
        AttachResponseDTO uploadFile = attachService.uploadFile(dto.getFile());
        product.setNameUz(dto.getName_uz());
        product.setNameRu(dto.getName_ru());
        product.setDescriptionUz(dto.getDescription_uz());
        product.setDescriptionRu(dto.getDescription_ru());
        product.setScore(dto.getScore());
        product.setAttachId(uploadFile.getId());
        product.setPrice(dto.getPrice());
        return product;
    }

    /**
     * This method is used for converting CreatedDto to ProductEntity
     * If Category not Found throw new NotFoundParentCategoryException
     *
     * @param dto ProductDto
     * @return ProductEntity
     */
    public ProductEntity getProductEntity(ProductDto dto, CategoryEntity categoryEntity) {
        AttachResponseDTO uploadFile = attachService.uploadFile(dto.getFile());
        ProductEntity product = new ProductEntity();
        product.setNameUz(dto.getName_uz());
        product.setNameRu(dto.getName_ru());
        product.setDescriptionUz(dto.getDescription_uz());
        product.setDescriptionRu(dto.getDescription_ru());
        product.setScore(dto.getScore());
        product.setAttachId(uploadFile.getId());
        product.setPrice(dto.getPrice());
        product.setCategory(categoryEntity);
        return product;
    }

    /**
     * This method is used for converting ProductEntity to responseProductEntity
     *
     * @param productEntity ProductEntity
     * @return ResponseProductDto
     */
    public ResponseProductDto responseProductDtoByLan(ProductEntity productEntity, Language language) {
        ResponseProductDto dto = new ResponseProductDto();

        if (language.equals(Language.UZ)) {
            dto.setName_uz(productEntity.getNameUz());
            dto.setDescription_uz(productEntity.getDescriptionUz());
        } else {
            dto.setName_ru(productEntity.getNameRu());
            dto.setDescription_ru(productEntity.getDescriptionRu());
        }
        dto.setId(productEntity.getId());
        dto.setScore(productEntity.getScore());
        dto.setPrice(productEntity.getPrice());
        dto.setPhotoUrl(UrlUtil.url + productEntity.getAttachId());
        return dto;
    }

    /**
     * This method is used for converting ProductEntity to ResponseProductDto
     *
     * @param productEntity ProductEntity
     * @return ResponseProductDto
     */
    public ResponseProductDto responseProductDto(ProductEntity productEntity) {
        ResponseProductDto dto = new ResponseProductDto();
        dto.setName_uz(productEntity.getNameUz());
        dto.setDescription_uz(productEntity.getDescriptionUz());
        dto.setName_ru(productEntity.getNameRu());
        dto.setDescription_ru(productEntity.getDescriptionRu());
        dto.setId(productEntity.getId());
        dto.setScore(productEntity.getScore());
        dto.setPrice(productEntity.getPrice());
        dto.setPhotoUrl(UrlUtil.url + productEntity.getAttachId());
        return dto;
    }

}
