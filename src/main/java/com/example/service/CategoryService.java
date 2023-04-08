package com.example.service;
import com.example.Exp.category.NotFoundParentCategoryId;
import com.example.dto.AttachResponseDTO;
import com.example.dto.CategoryCreationDTO;
import com.example.entity.CategoryEntity;
import com.example.enums.Language;
import com.example.repostoriy.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepostoriy;
    private final ResourceBundleService resourceBundleService;
    private final AttachService attachService;

    public CategoryService(CategoryRepository categoryRepository, ResourceBundleService resourceBundleService, AttachService attachService) {
        this.categoryRepostoriy = categoryRepository;
        this.resourceBundleService = resourceBundleService;
        this.attachService = attachService;
    }

    /**
     * this method is used to create a category and save it to the database
     *
     * @param dto categoryDTO
     * @param language language
     * @return getDTO(entity)
     */


    public CategoryCreationDTO create(CategoryCreationDTO dto, Language language) {

        CategoryEntity categoryEntity = new CategoryEntity();

        if (dto.getParentcategoryId() != null) {
            Optional<CategoryEntity> optional = categoryRepostoriy.findById(dto.getParentcategoryId());

            if (optional.isEmpty()) {
                throw new NotFoundParentCategoryId(resourceBundleService.getMessage("not.found.category.parentcategory.id",language.name()));
            }

            categoryEntity.setParentCategory(optional.get());
        }

        AttachResponseDTO attachResponseDTO = attachService.uploadFile(dto.getMultipartFile());

        categoryEntity.setNameRu(dto.getNameRu());
        categoryEntity.setNameUz(dto.getNameUz());
        categoryEntity.setAttachId(attachResponseDTO.getId());

        categoryRepostoriy.save(categoryEntity);

        return getDTO(categoryEntity);
    }

    /**
     * this method is used to wrap database data from entity to dto
     *
     * @param categoryEntity entity
     * @return categoryCreationDTO
     */

    private CategoryCreationDTO getDTO(CategoryEntity categoryEntity){

        CategoryCreationDTO categoryCreationDTO = new CategoryCreationDTO();
        categoryCreationDTO.setNameUz(categoryEntity.getNameUz());
        categoryCreationDTO.setNameRu(categoryEntity.getNameRu());
        categoryCreationDTO.setParentcategoryId(categoryCreationDTO.getParentcategoryId());
        return categoryCreationDTO;

    }
}