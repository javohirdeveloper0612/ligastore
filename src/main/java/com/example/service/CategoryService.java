package com.example.service;
import com.example.dto.AttachResponseDTO;
import com.example.dto.CategoryCreationDTO;
import com.example.dto.CategoryResponseListDTO;
import com.example.dto.ResponseCategoryDto;
import com.example.entity.CategoryEntity;
import com.example.enums.Language;
import com.example.exception.EmptyListException;
import com.example.exception.NotFoundParentCategory;
import com.example.repository.CategoryRepository;
import com.example.util.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final AttachService attachService;
    private final CategoryRepository categoryRepository;
    private final ResourceBundleService resourceBundleService;

    @Autowired
    public CategoryService(AttachService attachService, CategoryRepository categoryRepository, ResourceBundleService resourceBundleService) {
        this.attachService = attachService;
        this.categoryRepository = categoryRepository;
        this.resourceBundleService = resourceBundleService;
    }

    /**
     * This method is used to create a Category that is stored in the database
     *
     * @param dto ResponseCategoryDTO
     * @param language
     * @return
     */

    public ResponseCategoryDto add_category(CategoryCreationDTO dto, Language language) {

        CategoryEntity categoryEntity = new CategoryEntity();

        if (dto.getParentCategoryId() != null) {
            Optional<CategoryEntity> optionalCategory = categoryRepository.findById(dto.getParentCategoryId());

            if (optionalCategory.isEmpty()) {
                throw new NotFoundParentCategory(resourceBundleService.getMessage("parent.not.found", language));
            }

            categoryEntity.setParentCategory(optionalCategory.get());
        }

        AttachResponseDTO responseDTO = attachService.uploadFile(dto.getMultipartFile());

        categoryEntity.setNameUz(dto.getNameUz());
        categoryEntity.setNameRu(dto.getNameRu());
        categoryEntity.setAttachId(responseDTO.getId());
        CategoryEntity category = categoryRepository.save(categoryEntity);


        return new ResponseCategoryDto("Successfully saved Category", true, 201);

    }

    /**
     * this method is used to get the Category list if the list does not exist EmptyListException is thrown
     *
     * @param language language
     * @return dtoList
     */

    public List<CategoryResponseListDTO> getCategoryList(Language language) {

        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();

        if(categoryEntityList.isEmpty()){
            throw new EmptyListException(resourceBundleService.getMessage("category.empty.list", language));
        }

        List<CategoryResponseListDTO> dtoList = new LinkedList<>();
        categoryEntityList.forEach(categoryEntity -> dtoList.add(getCategoryByLang(categoryEntity,language)));

        return dtoList;
    }

    /**
     * this method fetches a list of categories from the base and populates the DTO according to the language and returns it
     * 
     * @param categoryEntity categoryEntity
     * @param language language
     * @return categoryDTO
     */

    public CategoryResponseListDTO getCategoryByLang(CategoryEntity categoryEntity ,Language language){

        CategoryResponseListDTO categoryDTO = new CategoryResponseListDTO();
        categoryDTO.setId(categoryEntity.getId());

        if(language.equals(Language.UZ)){

            categoryDTO.setName_uz(categoryEntity.getNameUz());

        }else {

            categoryDTO.setName_ru(categoryEntity.getNameRu());

        }

        categoryDTO.setPhoto_url(UrlUtil.url + categoryEntity.getAttachId());

        return categoryDTO;

    }
}