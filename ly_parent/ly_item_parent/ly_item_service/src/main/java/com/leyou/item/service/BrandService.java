package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.DTO.BrandDTO;
import com.leyou.exception.LyException;
import com.leyou.exception.enums.ResponseCode;
import com.leyou.exception.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.utils.BeanHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Transactional
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<BrandDTO> queryBrandByPage(Integer page, Integer rows, String key, String sortBy, Boolean desc) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNoneBlank(key)) {
            criteria.orLike("name","%"+key+"%").orEqualTo("letter",key.toUpperCase());
        }
        if (StringUtils.isNoneBlank(sortBy)) {
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);// id desc
        }
        // 查询
        List<Brand> brands = brandMapper.selectByExample(example);

        // 判断是否为空
        if(CollectionUtils.isEmpty(brands)){
            throw new LyException(ResponseCode.BRAND_NOT_FOUND);
        }

        // 解析分页结果
        PageInfo<Brand> info = new PageInfo<>(brands);

        // 转为BrandDTO
        List<BrandDTO> list = BeanHelper.copyWithCollection(brands, BrandDTO.class);
        return new PageResult<>(info.getTotal(),list);
    }

    public void saveBrand(BrandDTO brandDTO, List<Long> cids) {
        Brand brand = BeanHelper.copyProperties(brandDTO, Brand.class);
        brand.setId(null);
        int selective = brandMapper.insertSelective(brand);
        if (selective!= 1) {
            throw new LyException(ResponseCode.INSERT_OPERATION_FAIL);
        }
        int categoryBrand = brandMapper.insertCategoryBrand(brand.getId(), cids);
        if (categoryBrand != cids.size()) {
            throw new LyException(ResponseCode.INSERT_OPERATION_FAIL);
        }
    }

    public void updateBrand(BrandDTO brandDTO, List<Long> ids) {
        Brand brand = BeanHelper.copyProperties(brandDTO, Brand.class);
        // 修改品牌
        int count = brandMapper.updateByPrimaryKeySelective(brand);
        if(count != 1){
            // 更新失败，抛出异常
            throw new LyException(ResponseCode.UPDATE_OPERATION_FAIL);
        }
        // 删除中间表数据
        brandMapper.deleteCategoryBrand(brand.getId());
        count = brandMapper.insertCategoryBrand(brand.getId(), ids);
        if(count != ids.size()){
            // 新增失败，抛出异常
            throw new LyException(ResponseCode.INSERT_OPERATION_FAIL);
        }
    }
    public BrandDTO queryById(Long id) {
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if (brand == null) {
            throw new LyException(ResponseCode.BRAND_NOT_FOUND);
        }
        return BeanHelper.copyProperties(brand, BrandDTO.class);
    }

    public List<BrandDTO> queryByCategoryId(Long categoryId) {
        List<Brand> brandList = brandMapper.queryByCategoryId(categoryId);
        if (CollectionUtils.isEmpty(brandList)) {
            throw new LyException(ResponseCode.BRAND_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(brandList, BrandDTO.class);
    }
}
