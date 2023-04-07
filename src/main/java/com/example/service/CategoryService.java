package com.example.service;
import com.example.Exp.NotFoundParentCategoryId;
import com.example.dto.CategoryCreationDTO;
import com.example.entity.CategoryEntity;
import com.example.enums.Language;
import com.example.repostoriy.CategoryRepostoriy;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepostoriy categoryRepostoriy;
    private final ResourceBundleService resourceBundleService;

    public CategoryService(CategoryRepostoriy categoryRepostoriy, ResourceBundleService resourceBundleService) {
        this.categoryRepostoriy = categoryRepostoriy;
        this.resourceBundleService = resourceBundleService;
    }



    public CategoryCreationDTO create(CategoryCreationDTO dto, Language language) {

        CategoryEntity category = new CategoryEntity();

        if (dto.getParentcategoryId() != null) {
            Optional<CategoryEntity> optional = categoryRepostoriy.findById(dto.getParentcategoryId());

            if (optional.isEmpty()) {
                throw new NotFoundParentCategoryId(resourceBundleService.getMessage("not.found.category.parentcategory.id",language.name()));
            }

            category.setParentCategory(optional.get());
        }

        category.setNameRu(dto.getNameRu());
        category.setNameUz(dto.getNameUz());


        categoryRepostoriy.save()
    }
