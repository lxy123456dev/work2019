package com.leyou.dto;

import lombok.Data;

@Data
public class GoodsDTO {
    public Long id;  //商品ID
    public String skus;  //商品SKU列表 注意：是字符串
    public String subTitle;  //促销信息
}