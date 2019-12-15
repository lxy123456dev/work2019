package com.leyou;

import com.leyou.DTO.SkuDTO;
import com.leyou.DTO.SpecParamDTO;
import com.leyou.DTO.SpuDTO;
import com.leyou.DTO.SpuDetailDTO;
import com.leyou.exception.vo.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("item-service")
public interface ItemClient {
    @GetMapping("/spu/page")
    public PageResult<SpuDTO> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key

    );
    @GetMapping("sku/of/spu")
    public List<SkuDTO> querySkuBySpuId(@RequestParam("id") Long id);
    @GetMapping("/spec/params")
    public List<SpecParamDTO> querySpecParams(@RequestParam(value = "gid",required = false) Long gid,
                                                              @RequestParam(value = "cid",required = false)Long cid);
    @GetMapping("/spu/detail")
    public SpuDetailDTO querySpuDetailBySpuId(@RequestParam("id") Long spuId);
}
