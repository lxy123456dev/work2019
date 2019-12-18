package com.leyou.DTO;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SpecGroupDTO {
    private Long id;

    private Long cid;

    private String name;

    private List<SpecParamDTO> params;
}