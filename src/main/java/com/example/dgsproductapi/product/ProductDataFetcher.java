package com.example.dgsproductapi.product;


import com.example.dgsproductapi.generated.types.*;

import com.example.dgsproductapi.generated.types.Product;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;


@DgsComponent
@RequiredArgsConstructor
public class ProductDataFetcher {

    private final ProductService productService;

    @DgsQuery
    public ProductPage products(
            int page,
            int size,
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            ProductSortField sortField,
            SortDirection sortDirection
    ) {

        var result = productService.getPaginatedList(
                page,
                size,
                name,
                minPrice,
                maxPrice,
                sortField,
                sortDirection
        );

        return ProductPage.newBuilder()
                .content(
                        result.getContent()
                                .stream()
                                .map(this::toGraphQLProduct)
                                .toList()
                )
                .page(result.getNumber() + 1)
                .size(result.getSize())
                .totalElements((int) result.getTotalElements())
                .totalPages(result.getTotalPages())
                .first(result.isFirst())
                .last(result.isLast())
                .build();
    }

    @DgsQuery
    public Product productById(Long id) {

        return toGraphQLProduct(
                productService.findById(id)
        );
    }

    @DgsMutation
    public Product createProduct(
            CreateProductInput input
    ) {

        var product = productService.create(
                input.getName(),
                input.getDescription(),
                input.getPrice()
        );

        return toGraphQLProduct(product);
    }

    @DgsMutation
    public Product updateProduct(
            Long id,
            UpdateProductInput input
    ) {

        var product = productService.update(
                id,
                input.getName(),
                input.getDescription(),
                input.getPrice()
        );

        return toGraphQLProduct(product);
    }

    @DgsMutation
    public Boolean deleteProduct(Long id) {

        return productService.delete(id);
    }

    private Product toGraphQLProduct(
            com.example.dgsproductapi.product.Product entity
    ) {

        return Product.newBuilder()
                .id(entity.getId().toString())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .build();
    }
}