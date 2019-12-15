package com.leyou;

import com.leyou.DTO.SpuDTO;
import com.leyou.exception.vo.PageResult;
import com.leyou.pojo.Goods;
import com.leyou.repository.GoodsIndexRespository;
import com.leyou.service.GoodsIndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchApplication.class)
public class TestFeign {
    @Autowired
    private ItemClient itemClient;

    @Autowired
    private GoodsIndexService goodsIndexService;

    @Autowired
    private GoodsIndexRespository goodsIndexRespository;
    @Test
    public void test(){
        int page = 1;
        int size = 10;
        do {
            try {
                PageResult<SpuDTO> pageResult = itemClient.querySpuByPage(page, size,null,null);
                //处理返回SpuDTO对象
                List<SpuDTO> items = pageResult.getItems();
                List<Goods> list = new ArrayList<>();
                for (SpuDTO spuDTO : items) {
                    //调用 索引库业务对象 执行构建Goods对象
                    Goods goods = goodsIndexService.buildGoods(spuDTO);
                    list.add(goods);
                }
                goodsIndexRespository.saveAll(list);  //将产生Goods文档（记录）对象 保存到索引库
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            page++;
        }while (page<=20);

    }
}
