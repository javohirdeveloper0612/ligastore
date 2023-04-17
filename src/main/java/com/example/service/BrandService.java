package com.example.service;

import com.example.dto.attach.AttachResponseDTO;
import com.example.dto.brand.BrandDto;
import com.example.dto.brand.ResponseBrandDto;
import com.example.dto.jwt.ResponseMessage;
import com.example.entity.BrandEntity;
import com.example.enums.Language;
import com.example.exception.category.BrandNotFoundException;
import com.example.exception.category.EmptyListException;
import com.example.repository.BrandRepository;
import com.example.util.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class BrandService {
    private final BrandRepository brandRepository;
    private final ResourceBundleService resourceBundleService;
    private final AttachService attachService;

    @Autowired
    public BrandService(BrandRepository brandRepository, ResourceBundleService resourceBundleService,
                        AttachService attachService) {
        this.brandRepository = brandRepository;
        this.resourceBundleService = resourceBundleService;
        this.attachService = attachService;
    }

    /**
     * This method is used for adding new Brand
     *
     * @param brandDto BrandDto
     * @return ResponseBrandDto
     */
    public ResponseBrandDto addBrand(BrandDto brandDto) {
        BrandEntity savedBrand = brandRepository.save(getBrand(brandDto));
        return responseBrandDto(savedBrand);
    }

    /**
     * This method is used for getting all the brand data
     * if List is empty throw EmptyListException
     *
     * @param language Language
     * @return ResponseBrandDto
     */
    public List<ResponseBrandDto> getAllBrand(Language language) {
        List<BrandEntity> list = brandRepository.findAll();
        if (list.isEmpty())
            throw new EmptyListException(resourceBundleService.getMessage("brand.list.not.found", language));
        List<ResponseBrandDto> brandDtoList = new LinkedList<>();
        for (BrandEntity brandEntity : list)
            brandDtoList.add(responseBrandDtoByLan(brandEntity, language));
        return brandDtoList;
    }

    /**
     * This method is used for getting brand data by id
     * if not founded brand data throw new BrandNotFoundException
     *
     * @param brand_id Long
     * @param language Language
     * @return ResponseBrandDto
     */
    public ResponseBrandDto getBrandById(Long brand_id, Language language) {
        Optional<BrandEntity> brand = brandRepository.findById(brand_id);
        if (brand.isEmpty())
            throw new BrandNotFoundException(resourceBundleService.getMessage("brand.not.found", language));
        return responseBrandDtoByLan(brand.get(), language);
    }

    /**
     * This method is used for editing brand data
     * if not founded brand data throw new BrandNotFoundException
     *
     * @param brand_id Long
     * @param brandDto BrandDto
     * @param language Language
     * @return ResponseBrandDto
     */
    public ResponseBrandDto editeBrand(Long brand_id, Language language, BrandDto brandDto) {
        Optional<BrandEntity> optional = brandRepository.findById(brand_id);
        if (optional.isEmpty())
            throw new BrandNotFoundException(resourceBundleService.getMessage("brand.not.found", language));
        BrandEntity editedBrand = brandRepository.save(getBrand(optional.get(), brandDto));
        return responseBrandDtoByLan(editedBrand, language);
    }

    /**
     * This method is used for deleting brand data
     * if not founded the brand data throw new BrandNotFoundException
     *
     * @param brand_id Long
     * @param language Language
     * @return ResponseBrandDto
     */
    public ResponseMessage deleteBrand(Long brand_id, Language language) {
        Optional<BrandEntity> optional = brandRepository.findById(brand_id);
        if (optional.isEmpty())
            throw new BrandNotFoundException(resourceBundleService.getMessage("brand.not.found", language));
        brandRepository.delete(optional.get());
        return new ResponseMessage("Successfully deleted", true, 200);
    }


    /**
     * This method is used for converting BrandEntity to ResponseBrandDto
     *
     * @param brand    BrandEntity
     * @param language Language
     * @return ResponseBranddto
     */
    public ResponseBrandDto responseBrandDtoByLan(BrandEntity brand, Language language) {
        ResponseBrandDto responseBrandDto = new ResponseBrandDto();
        responseBrandDto.setId(brand.getId());
        if (language.equals(Language.UZ))
            responseBrandDto.setNameUz(brand.getNameUz());
        else
            responseBrandDto.setNameRu(brand.getNameRu());
        responseBrandDto.setFileUrl(UrlUtil.url + brand.getAttachId());
        return responseBrandDto;
    }

    /**
     * This method is used for converting BrandEntity to ResponseBrandDto
     *
     * @param brand BrandEntity
     * @return ResponseBrandDto
     */
    public ResponseBrandDto responseBrandDto(BrandEntity brand) {
        ResponseBrandDto responseBrandDto = new ResponseBrandDto();
        responseBrandDto.setId(brand.getId());
        responseBrandDto.setNameUz(brand.getNameUz());
        responseBrandDto.setNameRu(brand.getNameRu());
        responseBrandDto.setFileUrl(UrlUtil.url + brand.getAttachId());
        return responseBrandDto;
    }


    /**
     * This method is used for getting BrandEntity from BrandDto
     *
     * @param brandDto BrandDto
     * @return BrandEntity
     */
    public BrandEntity getBrand(BrandDto brandDto) {
        BrandEntity brand = new BrandEntity();
        brand.setNameUz(brandDto.getNameUz());
        brand.setNameRu(brandDto.getNameRu());
        AttachResponseDTO attach = attachService.uploadFile(brandDto.getMultipartFile());
        brand.setAttachId(attach.getId());
        return brand;
    }

    /**
     * This method is used for getting BrandEntity from BrandDto
     *
     * @param brandEntity BrandEntity
     * @param brandDto    Branddto
     * @return BrandEntity
     */
    public BrandEntity getBrand(BrandEntity brandEntity, BrandDto brandDto) {
        brandEntity.setNameUz(brandDto.getNameUz());
        brandEntity.setNameRu(brandDto.getNameRu());
        AttachResponseDTO attachResponseDTO = attachService.uploadFile(brandDto.getMultipartFile());
        brandEntity.setAttachId(attachResponseDTO.getId());
        return brandEntity;
    }
}
