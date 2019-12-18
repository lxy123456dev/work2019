package com.leyou.service;

import com.leyou.DTO.BrandDTO;
import com.leyou.DTO.CategoryDTO;
import com.leyou.DTO.SpecGroupDTO;
import com.leyou.DTO.SpuDTO;
import com.leyou.ItemClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageService {
    @Autowired
    private ItemClient itemClient;

    public Map<String, Object> loadItemData(Long id) {
        SpuDTO spu = itemClient.querySpuById(id);
        // 查询分类集合
        List<CategoryDTO> categories = itemClient.queryCategoryListByIds(spu.getCategoryIds());
        BrandDTO brand = itemClient.queryBrandById(spu.getBrandId());
        List<SpecGroupDTO> specs = itemClient.querySpecsByCid(spu.getCid3());
        Map<String, Object> data = new HashMap<>();
        data.put("categories", categories);
        data.put("brand", brand);
        data.put("spuName", spu.getName());
        data.put("subTitle", spu.getSubTitle());
        data.put("skus", spu.getSkus());
        data.put("detail", spu.getSpuDetail());
        data.put("specs", specs);
        return data;
    }
}
