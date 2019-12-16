package com.leyou.controller;

import com.leyou.dto.GoodsDTO;
import com.leyou.exception.vo.PageResult;
import com.leyou.service.GoodsIndexService;
import com.leyou.service.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SearchController {
    @Autowired
    private GoodsIndexService goodsIndexService;
    @PostMapping("/page")
    public ResponseEntity<PageResult<GoodsDTO>> searchByKey(@RequestBody SearchRequest searchRequest){
        return ResponseEntity.ok(goodsIndexService.searchByKey(searchRequest));
    }
    @PostMapping("/filter")
    public ResponseEntity<Map<String, List<?>>> searchFilter(@RequestBody SearchRequest searchRequest){
        return ResponseEntity.ok(goodsIndexService.searchFilter(searchRequest));
    }
}
