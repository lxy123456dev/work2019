package com.leyou.repository;


import com.leyou.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodsIndexRespository extends ElasticsearchRepository<Goods,Long> {
}
