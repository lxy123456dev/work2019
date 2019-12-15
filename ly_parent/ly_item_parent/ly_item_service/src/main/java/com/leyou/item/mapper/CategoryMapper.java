package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import com.leyou.mapper.MyBaseMappers;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends MyBaseMappers<Category> {
    List<Category> queryByBrandId(@Param("id") Long id);
}
