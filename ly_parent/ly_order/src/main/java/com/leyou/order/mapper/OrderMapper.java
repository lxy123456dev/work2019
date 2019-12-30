package com.leyou.order.mapper;


import com.leyou.order.pojo.Order;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface OrderMapper extends Mapper<Order>, IdListMapper<Order, Long>, InsertListMapper<Order> {


}
