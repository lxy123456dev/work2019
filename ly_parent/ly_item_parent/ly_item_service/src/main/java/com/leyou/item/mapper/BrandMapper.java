package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import com.leyou.mapper.MyBaseMappers;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestMapping;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends MyBaseMappers<Brand> {
    int insertCategoryBrand(@Param("bid")Long bid, @Param("cids") List<Long> cids);

    void deleteCategoryBrand(@Param("bid") Long bid);

    List<Brand> queryByCategoryId(Long categoryId);
}
