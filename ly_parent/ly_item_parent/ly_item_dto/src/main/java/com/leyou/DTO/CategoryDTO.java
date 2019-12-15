package com.leyou.DTO;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CategoryDTO {
    private Long id;
    private String name;
    private Long parentId;
    private Boolean isParent;
    private Integer sort;
}