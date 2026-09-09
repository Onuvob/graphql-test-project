package com.example.dgsproductapi.product;

import com.example.dgsproductapi.generated.types.ProductSortField;
import com.example.dgsproductapi.generated.types.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Page<Product> getPaginatedList(
            int page,
            int size,
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            ProductSortField sortField,
            SortDirection sortDirection
    ) {

        String sortProperty = getSortProperty(sortField);

        Sort.Direction direction =
                sortDirection == SortDirection.ASC
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortProperty);

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                sort
        );

        return productRepository.getPaginatedList(
                name,
                minPrice,
                maxPrice,
                pageable
        );
    }

    private String getSortProperty(ProductSortField sortField) {

        return switch (sortField) {
            case ID -> "id";
            case NAME -> "name";
            case PRICE -> "price";
        };
    }


    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(id)
                );
    }

    public Product create(
            String name,
            String description,
            BigDecimal price
    ) {
        Product product = Product.builder().name(name).description(description).price(price).build();

        return productRepository.save(product);
    }

    public Product update(
            Long id,
            String name,
            String description,
            BigDecimal price
    ) {
        Product product = findById(id);

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);

        return productRepository.save(product);
    }

    public boolean delete(Long id) {

        if (!productRepository.existsById(id)) {
            return false;
        }

        productRepository.deleteById(id);

        return true;
    }
}
