package com.example.dgsproductapi.product;

import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResDTO toDTO(Product product) {

        if (product == null) {
            return null;
        }

        return ProductResDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    public Product toEntity(ProductResDTO dto) {

        if (dto == null) {
            return null;
        }

        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build();
    }
}
