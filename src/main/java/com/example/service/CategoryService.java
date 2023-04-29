package com.example.service;

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
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CategoryService {

    private final AttachService attachService;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ResourceBundleService bundleService;

    @Autowired
    public CategoryService(AttachService attachService, CategoryRepository categoryRepository,
                           BrandRepository brandRepository, ResourceBundleService resourceBundleService) {
        this.attachService = attachService;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.bundleService = resourceBundleService;
    }

    /**
     * This method is used to create a Category that is stored in the database
     *
     * @param dto      ResponseCategoryDTO
     * @param language Language
     * @return ResponseCategoryDto
     */
    public ResponseCategoryDto add_category(CategoryDto dto, Language language) {
        var optionalBrand = brandRepository.findById(dto.getBrandId());
        if (optionalBrand.isEmpty())
            throw new BrandNotFoundException(bundleService.getMessage("brand.not.found", language));
        var savedCategory = categoryRepository.save(getCategory(dto, optionalBrand.get()));
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
        var responseDTO = attachService.uploadFile(dto.getMultipartFile());
        return new CategoryEntity(dto.getNameUz(), dto.getNameRu(), responseDTO.getId(), brand);
    }

    /**
     * This method is used for converting Category to ResponseCategoryDto
     *
     * @param category categoryEntity
     * @return ResponsecategoryDto
     */
    public ResponseCategoryDto responseCategoryDto(CategoryEntity category) {
        return new ResponseCategoryDto(category.getId(), category.getNameUz(), category.getNameRu(),
                UrlUtil.url + category.getAttachId());
    }

    /**
     * this method is used to get the Category list if the list does not exist EmptyListException is thrown
     *
     * @param id       Long
     * @param language language
     * @return dtoList
     */
    public List<ResponseCategoryDto> getCategoryList(Long id, Language language) {
        var list = categoryRepository.findAllByBrandId(id);
        if (list.isEmpty())
            throw new EmptyListException(bundleService.getMessage("category.not.found", language));
        var dtoList = new LinkedList<ResponseCategoryDto>();
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
        var optional = categoryRepository.findById(id);
        if (optional.isEmpty())
            throw new BrandNotFoundException(bundleService.getMessage("category.not.found", language));
        return responseCategoryDtoByLan(language, optional.get());
    }

    /**
     * this method is used to find the category by id and replace the not.found.category.id exception if the category id is not found
     *
     * @param id       Long
     * @param dto      categoryUpdateDTO
     * @param language language
     * @return categoryUpdateDTO
     */
    public ResponseCategoryDto editeCategory(Long id, EditeCategoryDto dto, Language language) {
        var optional = categoryRepository.findById(id);
        if (optional.isEmpty())
            throw new BrandNotFoundException(bundleService.getMessage("category.not.found", language));
        return responseCategoryDto(categoryRepository.save(getCategory(optional.get(), dto)));
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
        var optional = categoryRepository.findById(id);
        if (optional.isEmpty()) {
            throw new BrandNotFoundException(bundleService.getMessage("category.not.found", language));
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
        var dtoList = new LinkedList<ResponseCategoryDto>();
        var pageable = PageRequest.of(page, size);
        var entityPage = categoryRepository.findAll(pageable);
        if (entityPage.isEmpty())
            throw new EmptyListException(bundleService.getMessage("category.not.found", language));
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
        var dto = new ResponseCategoryDto();
        if (language.equals(Language.UZ)) dto.setNameUz(category.getNameUz());
        else dto.setNameRu(category.getNameRu());
        dto.setId(category.getId());
        dto.setPhotoUrl(UrlUtil.url + category.getAttachId());
        return dto;
    }


    /**
     * This method is used for  getting category
     *
     * @param category CategoryEntity
     * @param dto      EditeCategoryDto
     * @return CategoryEntity
     */
    public CategoryEntity getCategory(CategoryEntity category, EditeCategoryDto dto) {
        var attach = attachService.uploadFile(dto.getMultipartFile());
        category.setNameUz(dto.getNameUz());
        category.setNameRu(dto.getNameRu());
        category.setAttachId(attach.getId());
        return category;
    }

}