package com.leyou.order.mapper;

import com.leyou.order.pojo.OrderLogistics;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface OrderLogisticsMapper extends Mapper<OrderLogistics>, IdListMapper<OrderLogistics, Long>, InsertListMapper<OrderLogistics> {//OrderLogistics
}
