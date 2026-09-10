package com.example.dgsproductapi.product;

import com.example.dgsproductapi.generated.types.CreateProductInput;
import com.example.dgsproductapi.generated.types.ProductPage;
import com.example.dgsproductapi.generated.types.ProductSortField;
import com.example.dgsproductapi.generated.types.SortDirection;
import com.example.dgsproductapi.generated.types.UpdateProductInput;
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
                .content(result.getContent())
                .page(result.getNumber() + 1)
                .size(result.getSize())
                .totalElements((int) result.getTotalElements())
                .totalPages(result.getTotalPages())
                .first(result.isFirst())
                .last(result.isLast())
                .build();
    }

    @DgsQuery
    public ProductResDTO productById(Long id) {

        return productService.findById(id);
    }

    @DgsMutation
    public ProductResDTO createProduct(
            CreateProductInput input
    ) {

        return productService.create(
                input.getName(),
                input.getDescription(),
                input.getPrice()
        );
    }

    @DgsMutation
    public ProductResDTO updateProduct(
            Long id,
            UpdateProductInput input
    ) {

        return productService.update(
                id,
                input.getName(),
                input.getDescription(),
                input.getPrice()
        );
    }

    @DgsMutation
    public Boolean deleteProduct(Long id) {

        return productService.delete(id);
    }
}