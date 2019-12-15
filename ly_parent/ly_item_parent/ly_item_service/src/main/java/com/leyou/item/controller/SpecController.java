package com.leyou.item.controller;

import com.leyou.DTO.SpecGroupDTO;
import com.leyou.DTO.SpecParamDTO;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SpecController {
    @Autowired
    private SpecService specService;
    @GetMapping("/spec/groups/of/category")
    public ResponseEntity<List<SpecGroupDTO>> queryGroupByCategory(@RequestParam("id") Long id) {
        return ResponseEntity.ok(specService.queryGroupByCategory(id));
    }
    @GetMapping("/spec/params")
    public ResponseEntity<List<SpecParamDTO>> querySpecParams(@RequestParam(value = "gid",required = false) Long gid,
                                                              @RequestParam(value = "cid",required = false)Long cid) {
        return ResponseEntity.ok(specService.querySpecParams(gid,cid));
    }

    @PostMapping
    public ResponseEntity<SpecParamDTO> saveSpecParam(@RequestBody SpecParamDTO specParam) {
        specService.saveSpecParam(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/spec/group")
    public ResponseEntity<Void> saveGroup(@RequestBody SpecGroupDTO specGroupDTO) {
        specService.saveGroup(specGroupDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateSpecParam(@RequestBody SpecParamDTO specParam) {
        specService.updateSpecParam(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/spec/param/{id}")
    public ResponseEntity<Void> deleteSpecParam(@PathVariable("id")Long id) {
        specService.deleteSpecParam(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
