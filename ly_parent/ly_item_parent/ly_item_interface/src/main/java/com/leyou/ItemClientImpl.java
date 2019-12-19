package com.leyou;

import com.leyou.DTO.*;
import com.leyou.exception.vo.PageResult;

import java.util.List;

public class ItemClientImpl implements ItemClient{
    @Override
    public PageResult<SpuDTO> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        return null;
    }

    @Override
    public List<SkuDTO> querySkuBySpuId(Long id) {
        return null;
    }

    @Override
    public List<SpecParamDTO> querySpecParams(Long gid, Long cid, Boolean searching) {
        return null;
    }

    @Override
    public SpuDetailDTO querySpuDetailBySpuId(Long spuId) {
        return null;
    }

    @Override
    public List<CategoryDTO> queryCategoryListByIds(List<Long> categoryIds) {
        return null;
    }

    @Override
    public List<BrandDTO> queryBrandListByIds(List<Long> brandIdList) {
        return null;
    }

    @Override
    public SpuDTO querySpuById(Long id) {
        return null;
    }

    @Override
    public List<SpecGroupDTO> querySpecsByCid(Long id) {
        return null;
    }

    @Override
    public BrandDTO queryBrandById(Long brandId) {
        return null;
    }
}
