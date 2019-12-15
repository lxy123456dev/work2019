package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.DTO.*;
import com.leyou.exception.LyException;
import com.leyou.exception.enums.ResponseCode;
import com.leyou.exception.vo.PageResult;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.utils.BeanHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Transactional
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper detailMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    public PageResult<SpuDTO> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNoneBlank(key)) {
            criteria.andLike("name", "%" + key + "%");
        }
        if (saleable != null) {
            criteria.andEqualTo("saleable",saleable);
        }
        example.setOrderByClause("update_time DESC");
        List<Spu> spuList = spuMapper.selectByExample(example);
        PageInfo<Spu> info = new PageInfo<>(spuList);
        if (CollectionUtils.isEmpty(spuList)) {
            throw new LyException(ResponseCode.GOODS_NOT_FOUND);
        }
        List<SpuDTO> spuDTOS = BeanHelper.copyWithCollection(spuList, SpuDTO.class);
        CategoryAndBrand(Objects.requireNonNull(spuDTOS));
        return new PageResult<SpuDTO>(info.getTotal(),spuDTOS);
    }

    private void CategoryAndBrand(List<SpuDTO> list) {
        list.forEach(spu -> {
            List<CategoryDTO> categoryDTOList = categoryService.queryCategoryByIds(spu.getCategoryIds());
            String categoryName = categoryDTOList.stream().map(CategoryDTO::getName)
                    .collect(Collectors.joining("/"));
            spu.setCategoryName(categoryName);
            BrandDTO brand = brandService.queryById(spu.getBrandId());
            spu.setBrandName(brand.getName());
        });
    }

    public void saveGoods(SpuDTO spuDTO) {
        Spu spu = BeanHelper.copyProperties(spuDTO, Spu.class);
        spu.setSaleable(false);  //新增商品默认 下架状态
        //保存spu信息
        int count = spuMapper.insertSelective(spu);
        if (count != 1) {
            throw new LyException(ResponseCode.INSERT_OPERATION_FAIL);
        }
        //获取spuDtail保存spuDetail
        SpuDetail spuDetail = BeanHelper.copyProperties(spuDTO.getSpuDetail(), SpuDetail.class);
        spuDetail.setSpuId(spu.getId());  //商品详情spu_Detail关联spu
        count = spuDetailMapper.insertSelective(spuDetail);
        if (count != 1) {
            throw new LyException(ResponseCode.INSERT_OPERATION_FAIL);
        }
        //保存sku列表
        List<Sku> skuList = new ArrayList<>();
        List<SkuDTO> skus = spuDTO.getSkus();

        skus.forEach(skuDTO -> {
            //给sku设置逻辑外键
            skuDTO.setSpuId(spu.getId());
            skuDTO.setEnable(false);
            Sku sku = BeanHelper.copyProperties(skuDTO, Sku.class);
            sku.setCreateTime(new Date());
            sku.setUpdateTime(new Date());
            skuList.add(sku);
        });
        count = skuMapper.insertList(skuList);
        if(count!=skuList.size()){
            throw new LyException(ResponseCode.INSERT_OPERATION_FAIL);
        }
    }

    public void updateSaleable(Long id, Boolean saleable) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setSaleable(saleable);
        int count = spuMapper.updateByPrimaryKeySelective(spu);
        if (count != 1) {
            throw new LyException(ResponseCode.UPDATE_OPERATION_FAIL);
        }
        Sku sku = new Sku();
        sku.setEnable(saleable);
        // 2.2.准备更新的匹配条件
        Example example = new Example(Sku.class);
        example.createCriteria().andEqualTo("spuId", id);
        count = skuMapper.updateByExampleSelective(sku, example);

        int size = skuMapper.selectCountByExample(example);
        if (count != size) {
            throw new LyException(ResponseCode.UPDATE_OPERATION_FAIL);
        }
    }
    public SpuDetailDTO querySpuDetailBySpuId(Long spuId) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        if(spuDetail==null){
            throw new LyException(ResponseCode.GOODS_NOT_FOUND);
        }
        return BeanHelper.copyProperties(spuDetail, SpuDetailDTO.class);
    }
    public List<SkuDTO> querySkuListBySpuId(Long id) {
        Sku s = new Sku();
        s.setSpuId(id);
        List<Sku> list = skuMapper.select(s);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ResponseCode.GOODS_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(list, SkuDTO.class);
    }
    public void updateGoods(SpuDTO spuDTO) {
        Long spuId = spuDTO.getId();
        if (spuId == null) {
            // 请求参数有误
            throw new LyException(ResponseCode.INVALID_PARAM_ERROR);
        }
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        // 查询数量
        int size = skuMapper.selectCount(sku);
        if(size > 0) {
            int count = skuMapper.delete(sku);
            if(count != size){
                throw new LyException(ResponseCode.UPDATE_OPERATION_FAIL);
            }
        }

        // 2.更新spu
        Spu spu = BeanHelper.copyProperties(spuDTO, Spu.class);
        spu.setSaleable(false);
        spu.setCreateTime(new Date());
        spu.setUpdateTime(new Date());
        int count = spuMapper.updateByPrimaryKeySelective(spu);
        if (count != 1) {
            throw new LyException(ResponseCode.UPDATE_OPERATION_FAIL);
        }

        // 3.更新spuDetail
        SpuDetail spuDetail = BeanHelper.copyProperties(spuDTO.getSpuDetail(), SpuDetail.class);
        spuDetail.setSpuId(spuId);
        spuDetail.setCreateTime(null);
        spuDetail.setUpdateTime(null);
        count = detailMapper.updateByPrimaryKeySelective(spuDetail);
        if (count != 1) {
            throw new LyException(ResponseCode.UPDATE_OPERATION_FAIL);
        }

        // 4.新增sku
        List<SkuDTO> dtoList = spuDTO.getSkus();
        // 处理dto
        List<Sku> skuList = dtoList.stream().map(dto -> {
            dto.setEnable(false);
            // 添加spu的id
            dto.setSpuId(spuId);
            return BeanHelper.copyProperties(dto, Sku.class);
        }).collect(Collectors.toList());
        count = skuMapper.insertList(skuList);
        if (count != skuList.size()) {
            throw new LyException(ResponseCode.UPDATE_OPERATION_FAIL);
        }
    }
}
