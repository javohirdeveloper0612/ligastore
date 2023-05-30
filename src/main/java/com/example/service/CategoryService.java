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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    public ResponseCategoryDto add_category(CategoryDto dto, Language language) {
        var optionalBrand = brandRepository.findById(dto.getBrandId());
        if (optionalBrand.isEmpty())
            throw new BrandNotFoundException(bundleService.getMessage("brand.not.found", language));
        var savedCategory = categoryRepository.save(getCategory(dto, optionalBrand.get()));
        return responseCategoryDto(savedCategory);

    }


    public CategoryEntity getCategory(CategoryDto dto, BrandEntity brand) {
        var responseDTO = attachService.uploadFile(dto.getMultipartFile());
        return new CategoryEntity(dto.getNameUz(), dto.getNameRu(), responseDTO.getId(), brand);
    }


    public ResponseCategoryDto responseCategoryDto(CategoryEntity category) {
        return new ResponseCategoryDto(category.getId(), category.getNameUz(), category.getNameRu(),
                UrlUtil.url + category.getAttachId());
    }


    public List<ResponseCategoryDto> getCategoryList(Long id, Language language) {
        var list = categoryRepository.findAllByBrandId(id);
        if (list.isEmpty()) throw new EmptyListException(bundleService.getMessage("category.not.found", language));
        var dtoList = new LinkedList<ResponseCategoryDto>();
        list.forEach(categoryEntity -> dtoList.add(responseCategoryDtoByLan(language, categoryEntity)));
        return dtoList;
    }


    public ResponseCategoryDto getCategoryById(Long id, Language language) {
        var optional = categoryRepository.findById(id);
        if (optional.isEmpty()) throw new BrandNotFoundException(bundleService.getMessage("category.not.found", language));
        return responseCategoryDtoByLan(language, optional.get());
    }


    public ResponseCategoryDto editeCategory(Long id, EditeCategoryDto dto, Language language) {
        var optional = categoryRepository.findById(id);
        if (optional.isEmpty()) throw new BrandNotFoundException(bundleService.getMessage("category.not.found", language));
        return responseCategoryDto(categoryRepository.save(getCategory(optional.get(), dto)));
    }

    @Transactional
    public ResponseMessage deleteCategory(Long id, Language language) {
        var optional = categoryRepository.findById(id);
        if (optional.isEmpty()) throw new BrandNotFoundException(bundleService.getMessage("category.not.found", language));
        attachService.deleteById(optional.get().getAttachId());
        categoryRepository.deleteById(optional.get().getId());
        return new ResponseMessage("Successfully deleted", true, 200);
    }


    public ResponseCategoryDto responseCategoryDtoByLan(Language language, CategoryEntity category) {
        var dto = new ResponseCategoryDto();
        if (language.equals(Language.UZ)) dto.setNameUz(category.getNameUz());
        else dto.setNameRu(category.getNameRu());
        dto.setId(category.getId());
        dto.setPhotoUrl(UrlUtil.url + category.getAttachId());
        return dto;
    }


    public CategoryEntity getCategory(CategoryEntity category, EditeCategoryDto dto) {
        var attach = attachService.uploadFile(dto.getMultipartFile());
        category.setNameUz(dto.getNameUz());
        category.setNameRu(dto.getNameRu());
        category.setAttachId(attach.getId());
        return category;
    }

}