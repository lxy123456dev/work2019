package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category, Long> {
    List<Category> queryByBrandId(@Param("id") Long id);
}
