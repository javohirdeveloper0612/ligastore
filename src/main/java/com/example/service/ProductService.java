package com.example.service;

import com.example.dto.attach.AttachResponseDTO;
import com.example.dto.jwt.ResponseMessage;
import com.example.dto.product.ProductDto;
import com.example.dto.product.ResponseProductDto;
import com.example.entity.AdminMessageEntity;
import com.example.entity.CategoryEntity;
import com.example.entity.ProductEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.Language;
import com.example.exception.AlreadyProductModelException;
import com.example.exception.auth.ProfileNotFoundException;
import com.example.exception.category.EmptyListException;
import com.example.exception.category.NotFoundParentCategory;
import com.example.exception.product.ProductNotFoundException;
import com.example.repository.AdminMesageRepository;
import com.example.repository.CategoryRepository;
import com.example.repository.ProductRepository;
import com.example.repository.ProfileRepository;
import com.example.security.CustomUserDetail;
import com.example.util.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final AttachService attachService;
    private final CategoryRepository categoryRepository;
    private final ResourceBundleService resourceBundleService;
    private final ProfileRepository profileRepository;

    private final AdminMesageRepository productUserRepository;


    @Autowired
    public ProductService(ProductRepository productRepository, AttachService attachService,
                          CategoryRepository categoryRepository, ResourceBundleService resourceBundleService,
                          ProfileRepository profileRepository, AdminMesageRepository productUserRepository) {
        this.productRepository = productRepository;
        this.attachService = attachService;
        this.categoryRepository = categoryRepository;
        this.resourceBundleService = resourceBundleService;
        this.profileRepository = profileRepository;
        this.productUserRepository = productUserRepository;
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
        var optional = categoryRepository.findById(categoryId);
        if (optional.isEmpty())
            throw new NotFoundParentCategory(resourceBundleService.getMessage("category.not.found", language));

        if (productRepository.existsByModel(dto.getModel()))
            throw new AlreadyProductModelException(resourceBundleService.getMessage("model.exist", language));

        return responseProductDto(productRepository.save(getProductEntity(dto, optional.get())));
    }


    /**
     * This method is used for editing product data if product_id not founded
     * throw new ProductNotFoundException
     *
     * @param dto ProductDto
     * @param lan Language
     * @return ResponseProductDto
     */
    public ResponseProductDto editeProduct(Long productId, ProductDto dto, Language lan) {
        var optional = productRepository.findById(productId);
        if (optional.isEmpty())
            throw new ProductNotFoundException(resourceBundleService.getMessage("product.not.found", lan));
        return responseProductDto(productRepository.save(getProductEntity(dto, optional.get())));

    }


    /**
     * This method is used for viewing all the product data list if product list is empty
     * throw EmptyListException
     *
     * @param category_id Long
     * @param lan         Language
     * @return List<ResponseProductDto></>
     */
    public List<ResponseProductDto> getProductList(Long category_id, Language lan) {
        var optional = categoryRepository.findById(category_id);
        if (optional.isEmpty())
            throw new NotFoundParentCategory(resourceBundleService.getMessage("category.not.found", lan));
        var list = productRepository.findAllByCategoryId(category_id);
        if (list.isEmpty()) throw new EmptyListException(resourceBundleService.getMessage("empty.list.product", lan));
        return getProductEntityList(list, lan);
    }


    /**
     * This method is used for getting ProductEntity List
     *
     * @param list     ProductEntity
     * @param language Language
     * @return List<ResponseProductEntity></>
     */
    public List<ResponseProductDto> getProductEntityList(List<ProductEntity> list, Language language) {
        var dtoList = new ArrayList<ResponseProductDto>();
        for (ProductEntity product : list) {
            dtoList.add(responseProductDtoByLan(product, language));
        }
        return dtoList;
    }

    /**
     * This method is used for getting product data by page if list is empty
     * throw new EmptyListException
     *
     * @param page        int
     * @param size        int
     * @param lan         Language
     * @param category_id Long
     * @return List<ResponseProductDto></>
     */
    public List<ResponseProductDto> getProductListByPage(int page, int size, Language lan, Long category_id) {
        var pageable = PageRequest.of(page, size);
        var entityPage = productRepository.findAllByCategoryId(category_id, pageable);
        if (entityPage.isEmpty())
            throw new EmptyListException(resourceBundleService.getMessage("empty.list.product", lan));
        return getProductList(entityPage, lan);

    }

    /**
     * This method is used for converting CreatedDto to ProductEntity
     * If Category not Found throw new NotFoundParentCategoryException
     *
     * @param dto ProductDto
     * @return ProductEntity
     */
    public ProductEntity getProductEntity(ProductDto dto, ProductEntity product) {
        var uploadFile = attachService.uploadFile(dto.getFile());
        product.setNameUz(dto.getName_uz());
        product.setNameRu(dto.getName_ru());
        product.setDescriptionUz(dto.getDescription_uz());
        product.setDescriptionRu(dto.getDescription_ru());
        product.setScore((long) (dto.getPrice() * 0.0004));
        product.setAttachId(uploadFile.getId());
        product.setPrice(dto.getPrice());
        product.setModel(dto.getModel());
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
        var uploadFile = attachService.uploadFile(dto.getFile());
        var product = new ProductEntity();
        product.setNameUz(dto.getName_uz());
        product.setNameRu(dto.getName_ru());
        product.setDescriptionUz(dto.getDescription_uz());
        product.setDescriptionRu(dto.getDescription_ru());
        product.setScore((long) (dto.getPrice() * 0.0004));
        product.setAttachId(uploadFile.getId());
        product.setPrice(dto.getPrice());
        product.setModel(dto.getModel());
        product.setCategory(categoryEntity);
        return product;
    }

    /**
     * This method is used for converting ProductEntity to responseProductEntity
     *
     * @param product ProductEntity
     * @return ResponseProductDto
     */
    public ResponseProductDto responseProductDtoByLan(ProductEntity product, Language language) {
        var dto = new ResponseProductDto();
        if (language.equals(Language.UZ)) {
            dto.setName_uz(product.getNameUz());
            dto.setDescription_uz(product.getDescriptionUz());
        } else {
            dto.setName_ru(product.getNameRu());
            dto.setDescription_ru(product.getDescriptionRu());
        }
        dto.setId(product.getId());
        dto.setScore(product.getScore());
        dto.setPrice(product.getPrice());
        dto.setModel(product.getModel());
        dto.setPhotoUrl(UrlUtil.url + product.getAttachId());
        return dto;
    }

    /**
     * This method is used for converting ProductEntity to ResponseProductDto
     *
     * @param product ProductEntity
     * @return ResponseProductDto
     */
    public ResponseProductDto responseProductDto(ProductEntity product) {
        var dto = new ResponseProductDto();
        dto.setName_uz(product.getNameUz());
        dto.setDescription_uz(product.getDescriptionUz());
        dto.setName_ru(product.getNameRu());
        dto.setDescription_ru(product.getDescriptionRu());
        dto.setId(product.getId());
        dto.setScore(product.getScore());
        dto.setPrice(product.getPrice());
        dto.setModel(product.getModel());
        dto.setPhotoUrl(UrlUtil.url + product.getAttachId());
        return dto;
    }


    /**
     * This method is used for converting Page<ProductEntity></> to List<ResponseProductDto></>
     *
     * @param entityPage Page<ProductEntity></>
     * @param language   Language
     * @return List<ResponseProductDto></>
     */
    public List<ResponseProductDto> getProductList(Page<ProductEntity> entityPage, Language language) {
        var list = new LinkedList<ResponseProductDto>();
        for (ProductEntity productEntity : entityPage) {
            list.add(responseProductDtoByLan(productEntity, language));
        }
        return list;
    }

    /**
     * This method is used for viewing product data by Id
     * If not found product_id throw ProductNotFoundException
     *
     * @param productId Long
     * @param language  Language
     * @return ResponseProductDto
     */
    public ResponseProductDto getProductById(Long productId, Language language) {
        var optional = productRepository.findById(productId);
        if (optional.isEmpty())
            throw new ProductNotFoundException(resourceBundleService.getMessage("product.not.found", language));
        return responseProductDtoByLan(optional.get(), language);
    }


    /**
     * This method is used for deleting product data if not found product_id
     * throw new ProductNotFoundException
     *
     * @param productId Long
     * @param language  Language
     * @return ResponseMessage
     */
    @Transactional
    public ResponseMessage deleteProduct(Long productId, Language language) {
        var optional = productRepository.findById(productId);
        if (optional.isEmpty())
            throw new ProductNotFoundException(resourceBundleService.getMessage("product.not.found", language));
        attachService.deleteById(optional.get().getAttachId());
        productRepository.delete(optional.get());
        return new ResponseMessage("Successfully deleted", true, 200);
    }

    /**
     * This method is used for selling product
     *
     * @param product_model String
     * @return ResponseMessage
     */
    public ResponseMessage sellProduct(String product_model, Language language) {

        var optional = productRepository.findByModel(product_model);
        if (optional.isEmpty()) {
            throw new ProductNotFoundException(resourceBundleService.getMessage("product.not.found", language));
        }

        ProfileEntity user = getUser(language);
        ProductEntity product = optional.get();

        if (user.getScore() >= product.getScore()) {
            productUserRepository.save(getProductUser(user, product));
            return new ResponseMessage("So'rovingiz adminga xabar jo'natildi", true, 200);
        }
        return new ResponseMessage("Sizning balingiz yetarli emas", false, 400);
    }

    private AdminMessageEntity getProductUser(ProfileEntity user, ProductEntity product) {
        var productUser = new AdminMessageEntity();
        productUser.setUser_id(user.getId());
        productUser.setUser_name(user.getNameUz());
        productUser.setUser_surname(user.getSurnameUz());
        productUser.setPhone(user.getPhoneUser());
        productUser.setProduct_name(product.getNameUz());
        productUser.setProduct_model(product.getModel());
        return productUser;
    }


    /**
     * This method is used for getting current User
     *
     * @param language Language
     * @return ProfileEntity
     */
    public ProfileEntity getUser(Language language) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        var optional = profileRepository.findById(customUserDetail.getId());
        if (optional.isEmpty()) {
            throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found", language));
        }
        return optional.get();
    }


}
