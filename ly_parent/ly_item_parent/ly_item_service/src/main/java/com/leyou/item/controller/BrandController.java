package com.leyou.item.controller;

import com.leyou.DTO.BrandDTO;
import com.leyou.exception.vo.PageResult;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @GetMapping("/of/category")
    public ResponseEntity<List<BrandDTO>> queryByCategoryId(@RequestParam("id") Long categoryId){
        return ResponseEntity.ok(brandService.queryByCategoryId(categoryId));
    }
    @GetMapping("page")
    public ResponseEntity<PageResult<BrandDTO>> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1")Integer page,
            @RequestParam(value = "rows", defaultValue = "5")Integer rows,
            @RequestParam(value = "key", required = false)String key,
            @RequestParam(value = "sortBy", required = false)String sortBy,
            @RequestParam(value = "desc", defaultValue = "false")Boolean desc
    ) {

        return ResponseEntity.ok(brandService.queryBrandByPage(page,rows, key, sortBy, desc));
    }

    @PostMapping
    public ResponseEntity<Void> saveBrand(BrandDTO brand, @RequestParam("cids") List<Long> cids) {
        brandService.saveBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping
    public ResponseEntity<Void> updateBrand(BrandDTO brand, @RequestParam("cids") List<Long> ids) {
        brandService.updateBrand(brand, ids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
