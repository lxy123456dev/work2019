package com.leyou.item.pojo;

import java.util.Date;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_spec_param")
public class SpecParam {
    /**
     * 主键
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /**
     * 商品分类id
     */
    private Long cid;

    /**
     *
     */
    private Long groupId;

    /**
     * 参数名
     */
    private String name;

    /**
     * 是否是数字类型参数，true或false
     */
    private Boolean numeric;

    /**
     * 数字类型参数的单位，非数字类型可以为空
     */
    private String unit;

    /**
     * 是否是sku通用属性，true或false
     */
    private Boolean generic;

    /**
     * 是否用于搜索过滤，true或false
     */
    private Boolean searching;

    /**
     * 数值类型参数，如果需要搜索，则添加分段间隔值，如CPU频率间隔：0.5-1.0
     */
    private String segments;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;
}

