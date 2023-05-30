package com.example.service;

import com.example.dto.jwt.ResponseMessage;
import com.example.dto.product.ProductDto;
import com.example.dto.product.ResponseProductDto;
import com.example.entity.AdminMessageEntity;
import com.example.entity.CategoryEntity;
import com.example.entity.ProductEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.Language;
import com.example.exception.auth.ProfileNotFoundException;
import com.example.exception.category.EmptyListException;
import com.example.exception.category.NotFoundParentCategory;
import com.example.exception.product.AlreadyProductModelException;
import com.example.exception.product.ProductNotFoundException;
import com.example.repository.AdminMessageRepository;
import com.example.repository.CategoryRepository;
import com.example.repository.ProductRepository;
import com.example.repository.ProfileRepository;
import com.example.security.CustomUserDetail;
import com.example.util.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final AttachService attachService;
    private final CategoryRepository categoryRepository;
    private final ResourceBundleService resourceBundleService;
    private final ProfileRepository profileRepository;
    private final AdminMessageRepository adminMessageRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, AttachService attachService,
                          CategoryRepository categoryRepository, ResourceBundleService resourceBundleService,
                          ProfileRepository profileRepository, AdminMessageRepository productUserRepository) {
        this.productRepository = productRepository;
        this.attachService = attachService;
        this.categoryRepository = categoryRepository;
        this.resourceBundleService = resourceBundleService;
        this.profileRepository = profileRepository;
        this.adminMessageRepository = productUserRepository;
    }


    public ResponseProductDto addProduct(Long categoryId, ProductDto dto, Language language) {
        var optional = categoryRepository.findById(categoryId);
        if (optional.isEmpty()) throw new NotFoundParentCategory(resourceBundleService.getMessage("category.not.found", language));
        if (productRepository.existsByModel(dto.getModel())) throw new AlreadyProductModelException(resourceBundleService.getMessage("model.exist", language));
        return responseProductDto(productRepository.save(getProductEntity(dto, optional.get())));
    }


    public ResponseProductDto editeProduct(Long productId, ProductDto dto, Language lan) {
        var optional = productRepository.findById(productId);
        if (optional.isEmpty()) throw new ProductNotFoundException(resourceBundleService.getMessage("product.not.found", lan));
        return responseProductDto(productRepository.save(getProductEntity(dto, optional.get())));
    }


    public List<ResponseProductDto> getProductList(Long category_id, Language lan) {
        var optional = categoryRepository.findById(category_id);
        if (optional.isEmpty()) throw new NotFoundParentCategory(resourceBundleService.getMessage("category.not.found", lan));
        var list = productRepository.findAllByCategoryId(category_id);
        if (list.isEmpty()) throw new EmptyListException(resourceBundleService.getMessage("empty.list.product", lan));
        return getProductEntityList(list, lan);
    }


    public List<ResponseProductDto> getProductEntityList(List<ProductEntity> list, Language language) {
        var dtoList = new ArrayList<ResponseProductDto>();
        for (ProductEntity product : list) dtoList.add(responseProductDtoByLan(product, language));
        return dtoList;
    }


    public ProductEntity getProductEntity(ProductDto dto, ProductEntity product) {
        var uploadFile = attachService.uploadFile(dto.getFile());
        product.setNameUz(dto.getName_uz());
        product.setNameRu(dto.getName_ru());
        product.setDescriptionUz(dto.getDescription_uz());
        product.setDescriptionRu(dto.getDescription_ru());
        product.setScore(dto.getPrice() * 40);
        product.setAttachId(uploadFile.getId());
        product.setPrice(dto.getPrice());
        product.setModel(dto.getModel());
        product.setIsFamous(dto.getIsFamous());
        return product;
    }


    public ProductEntity getProductEntity(ProductDto dto, CategoryEntity categoryEntity) {
        var uploadFile = attachService.uploadFile(dto.getFile());
        var product = new ProductEntity();
        product.setNameUz(dto.getName_uz());
        product.setNameRu(dto.getName_ru());
        product.setDescriptionUz(dto.getDescription_uz());
        product.setDescriptionRu(dto.getDescription_ru());
        product.setScore(dto.getPrice() * 40);
        product.setAttachId(uploadFile.getId());
        product.setPrice(dto.getPrice());
        product.setModel(dto.getModel());
        product.setCategory(categoryEntity);
        product.setIsFamous(dto.getIsFamous());
        return product;
    }


    public ResponseProductDto responseProductDtoByLan(ProductEntity product, Language language) {
        var dto = new ResponseProductDto();
        if (language == Language.UZ) {
            dto.setName_uz(product.getNameUz());
            dto.setDescription_uz(product.getDescriptionUz());
        } else {
            dto.setName_ru(product.getNameRu());
            dto.setDescription_ru(product.getDescriptionRu());
        }
        dto.setId(product.getId());
        dto.setScore(product.getScore());
        dto.setPrice(product.getPrice() * 11500);
        dto.setModel(product.getModel());
        dto.setIsFamous(product.getIsFamous());
        dto.setPhotoUrl(UrlUtil.url + product.getAttachId());
        return dto;
    }


    public ResponseProductDto responseProductDto(ProductEntity product) {
        var dto = new ResponseProductDto();
        dto.setName_uz(product.getNameUz());
        dto.setDescription_uz(product.getDescriptionUz());
        dto.setName_ru(product.getNameRu());
        dto.setDescription_ru(product.getDescriptionRu());
        dto.setId(product.getId());
        dto.setScore(product.getScore());
        dto.setPrice(product.getPrice() * 11500);
        dto.setModel(product.getModel());
        dto.setIsFamous(product.getIsFamous());
        dto.setPhotoUrl(UrlUtil.url + product.getAttachId());
        return dto;
    }


    public ResponseProductDto getProductById(Long productId, Language language) {
        var optional = productRepository.findById(productId);
        if (optional.isEmpty()) throw new ProductNotFoundException(resourceBundleService.getMessage("product.not.found", language));
        return responseProductDtoByLan(optional.get(), language);
    }


    @Transactional
    public ResponseMessage deleteProduct(Long productId, Language language) {
        var optional = productRepository.findById(productId);
        if (optional.isEmpty()) throw new ProductNotFoundException(resourceBundleService.getMessage("product.not.found", language));
        attachService.deleteById(optional.get().getAttachId());
        productRepository.delete(optional.get());
        return new ResponseMessage("Successfully deleted", true, 200);
    }


    public ResponseMessage sellProduct(String product_model, Language language) {
        var optional = productRepository.findByModel(product_model);
        if (optional.isEmpty()) throw new ProductNotFoundException(resourceBundleService.getMessage("product.not.found", language));
        ProfileEntity user = getUser(language);
        ProductEntity product = optional.get();
        if (user.getScore() < product.getScore()) return new ResponseMessage("Sizning balingiz yetarli emas", false, 400);
        adminMessageRepository.save(getProductUser(user, product));
        return new ResponseMessage("So'rovingiz adminga xabar jo'natildi", true, 200);
    }

    private AdminMessageEntity getProductUser(ProfileEntity user, ProductEntity product) {
        var productUser = new AdminMessageEntity();
        productUser.setUserId(user.getId());
        productUser.setUser_name(user.getNameUz());
        productUser.setUser_surname(user.getSurnameUz());
        productUser.setPhone(user.getPhoneUser());
        productUser.setProduct_name(product.getNameUz());
        productUser.setProduct_model(product.getModel());
        productUser.setAccepted(false);
        productUser.setSellScore(productUser.getSellScore());
        return productUser;
    }


    public ProfileEntity getUser(Language language) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        var optional = profileRepository.findById(customUserDetail.getId());
        if (optional.isEmpty()) throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found", language));
        return optional.get();
    }


    public List<ResponseProductDto> getAllProduct(Language language) {
        var list = productRepository.findAllByOrderByIdDesc();
        if (list.isEmpty()) throw new ProductNotFoundException(resourceBundleService.getMessage("empty.list.product", language));
        List<ResponseProductDto> dtoList = new LinkedList<>();
        for (ProductEntity productEntity : list) dtoList.add(responseProductDtoByLan(productEntity, language));
        return dtoList;
    }
}
