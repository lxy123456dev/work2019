package com.leyou.item.service;

import com.leyou.DTO.SpecGroupDTO;
import com.leyou.DTO.SpecParamDTO;
import com.leyou.exception.LyException;
import com.leyou.exception.enums.ResponseCode;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.utils.BeanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class SpecService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;
    public List<SpecGroupDTO> queryGroupByCategory(Long id) {
        SpecGroup s = new SpecGroup();
        s.setCid(id);
        List<SpecGroup> list = specGroupMapper.select(s);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ResponseCode.BRAND_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(list, SpecGroupDTO.class);
    }

    public List<SpecParamDTO> querySpecParams(Long gid,Long cid,Boolean searching) {
        SpecParam s = new SpecParam();
        s.setGroupId(gid);
        s.setCid(cid);
        s.setSearching(searching);
        if (gid ==null&&cid==null) {
            throw new LyException(ResponseCode.ERROR);
        }
        List<SpecParam> list = specParamMapper.select(s);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ResponseCode.SPEC_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(list, SpecParamDTO.class);
    }
    @Transactional
    public void saveSpecParam(SpecParamDTO specParam) {
        SpecParam param = BeanHelper.copyProperties(specParam, SpecParam.class);
        param.setId(null);
        if (StringUtils.isEmpty(param)) {
            throw new LyException(ResponseCode.ERROR);
        }
        int count = specParamMapper.insertSelective(param);
        if (count !=1) {
            throw new LyException(ResponseCode.INSERT_OPERATION_FAIL);
        }
    }
    @Transactional
    public void updateSpecParam(SpecParamDTO specParam) {
        SpecParam param = BeanHelper.copyProperties(specParam, SpecParam.class);
        int i = specParamMapper.updateByPrimaryKeySelective(param);
        if (i != 1) {
            throw new LyException(ResponseCode.UPDATE_OPERATION_FAIL);
        }
    }
    @Transactional
    public void deleteSpecParam(Long id) {
        int i = specParamMapper.deleteByPrimaryKey(id);
        if (i != 1) {
            throw new LyException(ResponseCode.DELETE_OPERATION_FAIL);
        }
    }

    public void saveGroup(SpecGroupDTO specGroupDTO) {
        SpecGroup specGroup = BeanHelper.copyProperties(specGroupDTO, SpecGroup.class);
        int i = specGroupMapper.insertSelective(specGroup);
        if (i != 1) {
            throw new LyException(ResponseCode.INSERT_OPERATION_FAIL);
        }
    }

    public List<SpecGroupDTO> querySpecsByCid(Long id) {
        List<SpecGroupDTO> groupList = queryGroupByCategory(id);
        List<SpecParamDTO> params = querySpecParams(null, id, null);
        Map<Long, List<SpecParamDTO>> collect = params.stream().collect(Collectors.groupingBy(SpecParamDTO::getGroupId));
        groupList.forEach(specGroupDTO -> {
            specGroupDTO.setParams(collect.get(specGroupDTO.getId()));
        });
        return groupList;
    }
}
