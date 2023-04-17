package com.example.service;

import com.example.dto.attach.AttachResponseDTO;
import com.example.dto.category.CategoryDto;
import com.example.dto.category.EditeCategoryDto;
import com.example.dto.category.ResponseCategoryDto;
import com.example.dto.jwt.ResponseMessage;
import com.example.entity.BrandEntity;
import com.example.entity.CategoryEntity;
import com.example.enums.Language;
import com.example.exception.category.BrandNotFoundException;
import com.example.exception.category.EmptyListException;
import com.example.repository.BrandRepository;
import com.example.repository.CategoryRepository;
import com.example.util.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final AttachService attachService;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ResourceBundleService resourceBundleService;

    @Autowired
    public CategoryService(AttachService attachService, CategoryRepository categoryRepository,
                           BrandRepository brandRepository, ResourceBundleService resourceBundleService) {
        this.attachService = attachService;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.resourceBundleService = resourceBundleService;
    }

    /**
     * This method is used to create a Category that is stored in the database
     *
     * @param dto      ResponseCategoryDTO
     * @param language Language
     * @return ResponseCategoryDto
     */
    public ResponseCategoryDto add_category(CategoryDto dto, Language language) {
        Optional<BrandEntity> optionalBrand = brandRepository.findById(dto.getBrandId());
        if (optionalBrand.isEmpty())
            throw new BrandNotFoundException(resourceBundleService.getMessage("brand.not.found", language));
        CategoryEntity savedCategory = categoryRepository.save(getCategory(dto, optionalBrand.get()));
        return responseCategoryDto(savedCategory);

    }


    /**
     * This method is used for  getting category
     *
     * @param dto   CategoryDto
     * @param brand BrandEntity
     * @return CategoryEntity
     */
    public CategoryEntity getCategory(CategoryDto dto, BrandEntity brand) {
        CategoryEntity categoryEntity = new CategoryEntity();
        AttachResponseDTO responseDTO = attachService.uploadFile(dto.getMultipartFile());
        categoryEntity.setNameUz(dto.getNameUz());
        categoryEntity.setNameRu(dto.getNameRu());
        categoryEntity.setAttachId(responseDTO.getId());
        categoryEntity.setBrand(brand);
        return categoryEntity;
    }

    /**
     * This method is used for converting Category to ResponseCategoryDto
     *
     * @param category categoryEntity
     * @return ResponsecategoryDto
     */
    public ResponseCategoryDto responseCategoryDto(CategoryEntity category) {
        ResponseCategoryDto dto = new ResponseCategoryDto();
        dto.setId(category.getId());
        dto.setNameUz(category.getNameUz());
        dto.setNameRu(category.getNameRu());
        dto.setPhotoUrl(UrlUtil.url + category.getAttachId());
        return dto;
    }

    /**
     * this method is used to get the Category list if the list does not exist EmptyListException is thrown
     *
     * @param id       Long
     * @param language language
     * @return dtoList
     */
    public List<ResponseCategoryDto> getCategoryList(Long id, Language language) {
        List<CategoryEntity> list = categoryRepository.findAllByBrandId(id);
        if (list.isEmpty())
            throw new EmptyListException(resourceBundleService.getMessage("category.not.found", language));
        List<ResponseCategoryDto> dtoList = new LinkedList<>();
        list.forEach(categoryEntity -> dtoList.add(responseCategoryDtoByLan(language, categoryEntity)));
        return dtoList;
    }

    /**
     * this method searches for the category by id, if the id does not exist, a not.found.category.id exception is thrown
     *
     * @param id       Long
     * @param language language
     * @return category
     */
    public ResponseCategoryDto getCategoryById(Long id, Language language) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty())
            throw new BrandNotFoundException(resourceBundleService.getMessage("category.not.found", language));
        return responseCategoryDtoByLan(language, optional.get());
    }

    /**
     * this method is used to find the category by id and replace the not.found.category.id exception if the category id is not found
     *
     * @param id                Long
     * @param categoryUpdateDTO categoryUpdateDTO
     * @param language          language
     * @return categoryUpdateDTO
     */
    public ResponseCategoryDto editeCategory(Long id, EditeCategoryDto categoryUpdateDTO, Language language) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty())
            throw new BrandNotFoundException(resourceBundleService.getMessage("category.not.found", language));
        CategoryEntity save = categoryRepository.save(getCategory(optional.get(), categoryUpdateDTO));
        return responseCategoryDto(save);

    }

    /**
     * this method is used to search category by id and delete if id not found category.not.found.
     * an exception is thrown and the category is deleted from the database
     *
     * @param id       Long
     * @param language language
     * @return String result
     */
    public ResponseMessage deleteCategory(Long id, Language language) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty()) {
            throw new BrandNotFoundException(resourceBundleService.getMessage("category.not.found", language));
        }
        attachService.deleteById(optional.get().getAttachId());
        categoryRepository.deleteById(optional.get().getId());
        return new ResponseMessage("Successfully deleted", true, 200);

    }

    /**
     * this method is used to get the category list from the database in the pagination method,
     * if the category does not exist in the database, an empty.list exception is thrown
     *
     * @param page     int
     * @param size     int
     * @param language language
     * @return new PageImpl<>(dtoList,pageable,entityPage.getTotalElements());
     */

    public Page<ResponseCategoryDto> categoryGetPaginationList(int page, int size, Language language) {
        List<ResponseCategoryDto> dtoList = new LinkedList<>();
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryEntity> entityPage = categoryRepository.findAll(pageable);
        if (entityPage.isEmpty())
            throw new EmptyListException(resourceBundleService.getMessage("category.not.found", language));
        entityPage.forEach(categoryEntity -> dtoList.add(responseCategoryDtoByLan(language, categoryEntity)));
        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());

    }


    /**
     * This method is used for  converting category to ResponseCategoryDto By Language
     *
     * @param language Language
     * @param category CategoryEntity
     * @return ResponseCategoryDto
     */
    public ResponseCategoryDto responseCategoryDtoByLan(Language language, CategoryEntity category) {
        ResponseCategoryDto responseCategoryDto = new ResponseCategoryDto();
        if (language.equals(Language.UZ)) {
            responseCategoryDto.setNameUz(category.getNameUz());
        } else {
            responseCategoryDto.setNameRu(category.getNameRu());
        }
        responseCategoryDto.setId(category.getId());
        responseCategoryDto.setPhotoUrl(UrlUtil.url + category.getAttachId());
        return responseCategoryDto;
    }


    /**
     * This method is used for  getting category
     *
     * @param category         CategoryEntity
     * @param editeCategoryDto EditeCategoryDto
     * @return CategoryEntity
     */
    public CategoryEntity getCategory(CategoryEntity category, EditeCategoryDto editeCategoryDto) {
        AttachResponseDTO attach = attachService.uploadFile(editeCategoryDto.getMultipartFile());
        category.setNameUz(editeCategoryDto.getNameUz());
        category.setNameRu(editeCategoryDto.getNameRu());
        category.setAttachId(attach.getId());
        return category;
    }

}