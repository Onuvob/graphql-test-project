package com.example.dgsproductapi.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
}
