package com.leyou.item.pojo;

import java.util.Date;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_spec_group")
public class SpecGroup {
    /**
     * 主键
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /**
     * 商品分类id，一个分类下有多个规格组
     */
    private Long cid;

    /**
     * 规格组的名称
     */
    private String name;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;
}

