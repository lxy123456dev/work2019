package com.leyou.item.controller;

import com.leyou.DTO.CategoryDTO;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping(value = "/category/of/parent")//,produces = "application/json;charset=UTF-8"
    public ResponseEntity<List<CategoryDTO>> queryByParentId(@RequestParam("pid") Long pId){
        List<CategoryDTO> list = categoryService.queryByParentId(pId);
        System.err.println(list);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/category/of/brand")
    public ResponseEntity<List<CategoryDTO>> changeByBrandId(@RequestParam("id")Long id) {
        return ResponseEntity.ok(categoryService.queryListByBrandId(id));
    }

}
