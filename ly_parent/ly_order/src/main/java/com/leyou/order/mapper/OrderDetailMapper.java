package com.leyou.order.mapper;

import com.leyou.order.pojo.OrderDetail;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;

public interface OrderDetailMapper extends Mapper<OrderDetail>, IdListMapper<OrderDetail, Long>, InsertListMapper<OrderDetail> {

    int insertDetailList(@Param("details") List<OrderDetail> details);

}
