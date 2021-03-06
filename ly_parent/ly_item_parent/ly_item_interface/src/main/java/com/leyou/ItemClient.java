package com.leyou;

import com.leyou.DTO.*;
import com.leyou.exception.vo.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "item-service",fallback = ItemClientImpl.class)
public interface ItemClient {
    @GetMapping("/spu/page")
    PageResult<SpuDTO> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key

    );
    @GetMapping("sku/of/spu")
    List<SkuDTO> querySkuBySpuId(@RequestParam("id") Long id);
    @GetMapping("/spec/params")
    List<SpecParamDTO> querySpecParams(@RequestParam(value = "gid", required = false) Long gid,
                                       @RequestParam(value = "cid", required = false) Long cid,
                                       @RequestParam(value = "searching", required = false) Boolean searching);
    @GetMapping("/spu/detail")
    SpuDetailDTO querySpuDetailBySpuId(@RequestParam("id") Long spuId);

    @GetMapping("/category/list")
    List<CategoryDTO> queryCategoryListByIds(@RequestParam("ids") List<Long> categoryIds);
    @GetMapping("/brand/list")
    List<BrandDTO> queryBrandListByIds(@RequestParam("ids") List<Long> brandIdList);
    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    SpuDTO querySpuById(@PathVariable("id") Long id);
    @GetMapping("/spec/of/category")
    List<SpecGroupDTO> querySpecsByCid(@RequestParam("id") Long id);
    @GetMapping("/brand/{id}")
    BrandDTO queryBrandById(@PathVariable("id") Long brandId);

    @GetMapping("sku/list")
    List<SkuDTO> querySkuByIds(@RequestParam("ids") List<Long> ids);
}
