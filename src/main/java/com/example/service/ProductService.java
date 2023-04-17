package com.example.service;

import com.example.dto.jwt.ResponseMessage;
import com.example.dto.attach.AttachResponseDTO;
import com.example.dto.product.ProductDto;
import com.example.dto.product.ResponseProductDto;
import com.example.entity.CategoryEntity;
import com.example.entity.ProductEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.Language;
import com.example.exception.auth.ProfileNotFoundException;
import com.example.exception.category.EmptyListException;
import com.example.exception.category.NotFoundParentCategory;
import com.example.exception.product.ProductNotFoundException;
import com.example.repository.CategoryRepository;
import com.example.repository.ProductRepository;
import com.example.repository.ProfileRepository;
import com.example.security.CustomUserDetail;
import com.example.telegrambot.myTelegrambot.MyTelegramBot;
import com.example.telegrambot.util.InlineButton;
import com.example.telegrambot.util.SendMsg;
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
    public static int orderId = 1;
    private final ProductRepository productRepository;
    private final AttachService attachService;
    private final CategoryRepository categoryRepository;
    private final ResourceBundleService resourceBundleService;
    private final ProfileRepository profileRepository;
    private final MyTelegramBot myTelegramBot;


    @Autowired
    public ProductService(ProductRepository productRepository, AttachService attachService,
                          CategoryRepository categoryRepository, ResourceBundleService resourceBundleService, ProfileRepository profileRepository, MyTelegramBot myTelegramBot) {
        this.productRepository = productRepository;
        this.attachService = attachService;
        this.categoryRepository = categoryRepository;
        this.resourceBundleService = resourceBundleService;

        this.profileRepository = profileRepository;
        this.myTelegramBot = myTelegramBot;
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
        if (optional.isEmpty())
            throw new NotFoundParentCategory(resourceBundleService.getMessage("category.not.found", language));
        ProductEntity savedProduct = productRepository.save(getProductEntity(dto, optional.get()));
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
        if (optional.isEmpty())
            throw new ProductNotFoundException(resourceBundleService.getMessage("product.not.found", language));
        ProductEntity editedProduct = productRepository.save(getProductEntity(productDto, optional.get()));
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
        if (optional.isEmpty())
            throw new NotFoundParentCategory(resourceBundleService.getMessage("category.not.found", language));
        List<ProductEntity> list = productRepository.findAllByCategoryId(category_id);
        if (list.isEmpty())
            throw new EmptyListException(resourceBundleService.getMessage("empty.list.product", language));
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
     * This method is used for getting product data by page if list is empty
     * throw new EmptyListException
     *
     * @param page        int
     * @param size        int
     * @param language    Language
     * @param category_id Long
     * @return List<ResponseProductDto></>
     */
    public List<ResponseProductDto> getProductListByPage(int page, int size, Language language, Long category_id) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductEntity> entityPage = productRepository.findAllByCategoryId(category_id, pageable);
        if (entityPage.isEmpty())
            throw new EmptyListException(resourceBundleService.getMessage("empty.list.product", language));
        return getProductList(entityPage, language);

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
        product.setScore((long) (dto.getPrice() * 0.00004));
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
        AttachResponseDTO uploadFile = attachService.uploadFile(dto.getFile());
        ProductEntity product = new ProductEntity();
        product.setNameUz(dto.getName_uz());
        product.setNameRu(dto.getName_ru());
        product.setDescriptionUz(dto.getDescription_uz());
        product.setDescriptionRu(dto.getDescription_ru());
        product.setScore((long) (dto.getPrice() * 0.00004));
        product.setAttachId(uploadFile.getId());
        product.setPrice(dto.getPrice());
        product.setModel(dto.getModel());
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
        dto.setModel(productEntity.getModel());
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
        dto.setModel(productEntity.getModel());
        dto.setPhotoUrl(UrlUtil.url + productEntity.getAttachId());
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
        List<ResponseProductDto> list = new LinkedList<>();
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
        Optional<ProductEntity> optional = productRepository.findById(productId);
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
        Optional<ProductEntity> optional = productRepository.findById(productId);
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
        ProfileEntity user = getUser(language);
        Optional<ProductEntity> optional = productRepository.findByModel(product_model);
        if (optional.isEmpty()) {
            throw new ProductNotFoundException(resourceBundleService.getMessage("product.not.found", language));
        }
        ProductEntity product = optional.get();
        if (user.getScore() >= product.getScore()) {
            sendMessage(user, optional.get());
            orderId++;
            return new ResponseMessage("Message sent To Admin and in Process", true, 200);
        }
        return new ResponseMessage("User's score is less than Product's score", false, 400);

    }

    /**
     * This method is used for getting current User
     *
     * @param language Language
     * @return ProfileEntity
     */
    public ProfileEntity getUser(Language language) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        Optional<ProfileEntity> optional = profileRepository.findById(customUserDetail.getId());
        if (optional.isEmpty()) {
            throw new ProfileNotFoundException(resourceBundleService.getMessage("profile.not.found", language));
        }
        return optional.get();
    }

    /**
     * This method is used for send messsage to admin
     *
     * @param user    ProfileEntity
     * @param product ProductEntity
     */
    public void sendMessage(ProfileEntity user, ProductEntity product) {
        myTelegramBot.send(SendMsg.sendMsg(
                1030035146L, "*Buyurtma raqami : * " + orderId +
                        "\n*Buyurtma beruvchining FIO :*" + user.getNameUz() + " " + user.getSurnameUz() +
                        "\n*Telefon raqami : *" + user.getPhoneUser() +
                        "\n*Product nomi : *" + product.getNameUz() +
                        "\n*Product modeli : *" + product.getModel(),
                InlineButton.keyboardMarkup(
                        InlineButton.rowList(InlineButton.row(
                                InlineButton.button("Qabul qilish", "accept"),
                                InlineButton.button("Rad qilish", "reject")
                        )))
        ));

    }

}
