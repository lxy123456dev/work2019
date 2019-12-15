package com.leyou.item.service;

import com.leyou.DTO.CategoryDTO;
import com.leyou.exception.LyException;
import com.leyou.exception.enums.ResponseCode;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import com.leyou.utils.BeanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<CategoryDTO> queryByParentId(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        List<Category> categoryList = categoryMapper.select(category);
        if (StringUtils.isEmpty(pid)) {
            throw new LyException(ResponseCode.PID_IS_NULL);
        }
        if(CollectionUtils.isEmpty(categoryList)){
            throw new LyException(ResponseCode.CATEGORY_NOT_FOUND);
        }
        System.out.println(BeanHelper.copyWithCollection(categoryList,CategoryDTO.class));
        return BeanHelper.copyWithCollection(categoryList,CategoryDTO.class);
    }

    public List<CategoryDTO> queryListByBrandId(Long id) {
        List<Category> list = categoryMapper.queryByBrandId(id);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ResponseCode.CATEGORY_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(list,CategoryDTO.class);
    }
    public List<CategoryDTO> queryCategoryByIds(List<Long> ids){
        List<Category> list = categoryMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ResponseCode.CATEGORY_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(list, CategoryDTO.class);
    }
}
