package com.leyou.mq;

import com.leyou.constants.MQConstants;
import com.leyou.service.GoodsIndexService;
import org.elasticsearch.search.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemListener {
@Autowired
private GoodsIndexService goodsIndexService;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name =MQConstants.Queue.SEARCH_ITEM_UP, durable = "true"),
            exchange = @Exchange(
                    name = MQConstants.Exchange.ITEM_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = MQConstants.RoutingKey.ITEM_UP_KEY))
    public void listenInsert(Long id){
        if(id != null){
            goodsIndexService.createIndex(id);
        }
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name =MQConstants.Queue.SEARCH_ITEM_DOWN, durable = "true"),
            exchange = @Exchange(
                    name = MQConstants.Exchange.ITEM_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = MQConstants.RoutingKey.ITEM_DOWN_KEY))
    public void listenDelete(Long id){
        if(id != null){
            goodsIndexService.deleteById(id);
        }
    }
}
