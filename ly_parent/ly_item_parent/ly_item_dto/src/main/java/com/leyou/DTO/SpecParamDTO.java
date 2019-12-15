package com.leyou.DTO;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SpecParamDTO {
    private Long id;
    private Long cid;
    private Long groupId;
    private String name;
    private Boolean numeric;
    private String unit;
    private Boolean generic;
    private Boolean searching;
    private String segments;
}