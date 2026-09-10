package com.example.dgsproductapi.product;

import com.example.dgsproductapi.generated.types.ProductSortField;
import com.example.dgsproductapi.generated.types.SortDirection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public Page<ProductResDTO> getPaginatedList(
            int page,
            int size,
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            ProductSortField sortField,
            SortDirection sortDirection
    ) {

//        if (page < 1) {
//            throw new IllegalArgumentException(
//                    "Page must be greater than or equal to 1"
//            );
//        }
//
//        if (size < 1 || size > 100) {
//            throw new IllegalArgumentException(
//                    "Size must be between 1 and 100"
//            );
//        }

        String sortProperty = getSortProperty(sortField);

        Sort.Direction direction =
                sortDirection == SortDirection.ASC
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(direction, sortProperty)
        );

        return productRepository.getPaginatedList(
                name,
                minPrice,
                maxPrice,
                pageable
        );
    }

    private String getSortProperty(ProductSortField sortField) {

        if (sortField == null) {
            return "id";
        }

        return switch (sortField) {
            case ID -> "id";
            case NAME -> "name";
            case PRICE -> "price";
        };
    }

    @Transactional(readOnly = true)
    public ProductResDTO findById(Long id) {

        return productRepository.findById(id)
                .map(productMapper::toDTO)
                .orElseThrow(() ->
                        new ProductNotFoundException(id)
                );
    }

    public ProductResDTO create(
            String name,
            String description,
            BigDecimal price
    ) {

        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .build();

        Product savedProduct = productRepository.save(product);

        return productMapper.toDTO(savedProduct);
    }

    public ProductResDTO update(
            Long id,
            String name,
            String description,
            BigDecimal price
    ) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(id)
                );

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);

        Product updatedProduct = productRepository.save(product);

        return productMapper.toDTO(updatedProduct);
    }

    public boolean delete(Long id) {

        if (!productRepository.existsById(id)) {
            return false;
        }

        productRepository.deleteById(id);

        return true;
    }
}