package com.leyou.DTO;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BrandDTO {
    private Long id;
    private String name;
    private String image;
    private Character letter;
}