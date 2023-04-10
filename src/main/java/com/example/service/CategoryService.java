package com.example.service;
import com.example.dto.attach.AttachResponseDTO;
import com.example.dto.category.CategoryCreationDTO;
import com.example.dto.category.CategoryResponseListDTO;
import com.example.dto.category.CategoryUpdateDTO;
import com.example.dto.category.ResponseCategoryDto;
import com.example.entity.CategoryEntity;
import com.example.enums.Language;
import com.example.exception.category.EmptyListException;
import com.example.exception.category.NotFoundCategoryId;
import com.example.exception.category.NotFoundParentCategoryId;
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
                throw new NotFoundParentCategoryId(resourceBundleService.getMessage("parent.not.found", language));
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
            throw new EmptyListException(resourceBundleService.getMessage("empty.list", language));
        }

        List<CategoryResponseListDTO> dtoList = new LinkedList<>();
        categoryEntityList.forEach(categoryEntity -> dtoList.add(getCategoryByLang(categoryEntity,language)));

        return dtoList;
    }

    /**
     * this method searches for the category by id, if the id does not exist, a not.found.category.id exception is thrown
     *
     * @param id Long
     * @param language language
     * @return category
     */

    public CategoryResponseListDTO getCategoryById(Long id, Language language) {

        Optional<CategoryEntity> optional = categoryRepository.findById(id);

        if(optional.isEmpty()){
            throw new NotFoundCategoryId(resourceBundleService.getMessage("not.found.category.id",language));
        }

        return getCategoryByLang(optional.get(),language);
    }

    /**
     * this method is used to find the category by id and replace the not.found.category.id exception if the category id is not found
     *
     * @param id Long
     * @param categoryUpdateDTO categoryUpdateDTO
     * @param language language
     * @return categoryUpdateDTO
     */


    public CategoryUpdateDTO categoryUpdate(Long id, CategoryUpdateDTO categoryUpdateDTO, Language language) {

        Optional<CategoryEntity> optional = categoryRepository.findById(id);

        if(optional.isEmpty()){
            throw new NotFoundCategoryId(resourceBundleService.getMessage("not.found.category.id",language));
        }

        CategoryEntity entity = optional.get();
        entity.setNameUz(categoryUpdateDTO.getNameUz());
        entity.setNameRu(categoryUpdateDTO.getNameRu());

        categoryRepository.save(entity);
        return categoryUpdateDTO;

    }

    /**
     * this method is used to search category by id and delete if id not found category.not.found. an exception is thrown and the category is deleted from the database
     *
     * @param id Long
     * @param language language
     * @return String result
     */

    public String categoryDelete(Long id, Language language) {

        Optional<CategoryEntity> optional = categoryRepository.findById(id);

        if(optional.isEmpty()){
            throw new NotFoundCategoryId(resourceBundleService.getMessage("not.found.category.id",language));
        }

        categoryRepository.deleteById(optional.get().getId());
        return "Category has been deleted";

    }

    /**
     * this method is used to get the category list from the database in the pagination method,
     * if the category does not exist in the database, an empty.list exception is thrown
     *
     * @param page int
     * @param size int
     * @param language language
     * @return new PageImpl<>(dtoList,pageable,entityPage.getTotalElements());
     */

    public Page<CategoryResponseListDTO> categoryGetPaginationList(int page, int size, Language language) {

        List<CategoryResponseListDTO> dtoList = new LinkedList<>();

        Pageable pageable = PageRequest.of(page,size);

        Page<CategoryEntity> entityPage = categoryRepository.findAll(pageable);

        if(entityPage.isEmpty()){
            throw new EmptyListException(resourceBundleService.getMessage("empty.list", language));
        }

        entityPage.forEach(categoryEntity -> dtoList.add(getCategoryByLang(categoryEntity,language)));

        return new PageImpl<>(dtoList,pageable,entityPage.getTotalElements());

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