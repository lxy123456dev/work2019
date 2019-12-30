package com.leyou.order.service;


import com.leyou.DTO.SkuDTO;
import com.leyou.ItemClient;
import com.leyou.exception.LyException;
import com.leyou.exception.enums.ResponseCode;
import com.leyou.order.dto.CartDTO;
import com.leyou.order.dto.OrderDTO;
import com.leyou.order.enums.OrderStatusEnum;
import com.leyou.order.mapper.OrderDetailMapper;
import com.leyou.order.mapper.OrderLogisticsMapper;
import com.leyou.order.mapper.OrderMapper;
import com.leyou.order.pojo.Order;
import com.leyou.order.pojo.OrderDetail;
import com.leyou.order.pojo.OrderLogistics;
import com.leyou.order.vo.OrderDetailVO;
import com.leyou.order.vo.OrderLogisticsVO;
import com.leyou.order.vo.OrderVO;
import com.leyou.threadlocals.UserHolder;
import com.leyou.user.AddressDTO;
import com.leyou.user.UserClient;
import com.leyou.utils.BeanHelper;
import com.leyou.utils.IdWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private ItemClient itemClient;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper detailMapper;

    @Autowired
    private UserClient userClient;

    @Autowired
    private OrderLogisticsMapper orderLogisticsMapper;

    /**
     * 新增订单
     *
     * @param orderDTO
     * @return
     */
    @Transactional
    public Long createOrder(OrderDTO orderDTO) {
        //1、组装订单对象Order 保存订单
        Order order = new Order();
        Long orderId = idWorker.nextId();
        order.setOrderId(orderId);
        //设置用户ID
        Long userId = UserHolder.getUser();
        order.setUserId(userId);
        //支付类型 默认微信支付
        order.setPaymentType(orderDTO.getPaymentType());
        //发票类型
        order.setInvoiceType(0);
        order.setCreateTime(new Date());
        //活动ID
        order.setPromotionIds("1,2");
        //订单来源
        order.setSourceType(2);
        //订单状态 : 创建订单 初始化，未支付
        order.setStatus(OrderStatusEnum.INIT.value());
        //订单运费
        order.setPostFee(600L);
        //订单总金额 累加 商品单价*数量
        Long total = 0L;

        List<CartDTO> carts = orderDTO.getCarts();
        //获取订单中包含商品ID 以及数量  将List转为Map<商品SkuID，商品数量>  方便通过skuID 获取商品数量
        Map<Long, Integer> skuIdNumMap = carts.stream().collect(Collectors.toMap(CartDTO::getSkuId, CartDTO::getNum));

        //远程调用商品微服务 根据商品SkuId集合 查询 sku集合
        List<Long> skuIdsList = carts.stream().map(CartDTO::getSkuId).collect(Collectors.toList());
        List<SkuDTO> skuDTOList = itemClient.querySkuByIds(skuIdsList);

        List<OrderDetail> details = new ArrayList<>();
        for (SkuDTO skuDTO : skuDTOList) {
            //获取商品单价 * 数量
            total += skuDTO.getPrice() * skuIdNumMap.get(skuDTO.getId());
            //遍历商品同时创建商品详情对象 将对象存入List
            //2、获取订单中商品，向订单详情表中增加数据-根据提交商品有关
            OrderDetail detail = new OrderDetail();
            detail.setOrderId(orderId);
            detail.setTitle(skuDTO.getTitle());
            detail.setPrice(skuDTO.getPrice());
            detail.setOwnSpec(skuDTO.getOwnSpec());
            detail.setNum(skuIdNumMap.get(skuDTO.getId()));
            detail.setImage(StringUtils.substringBefore(skuDTO.getImages(), ","));
            detail.setSkuId(skuDTO.getId());
            details.add(detail);
        }

        order.setTotalFee(total);
        //实际支付金额 = 总金额+运费-优惠金额
        order.setActualFee(total + order.getPostFee());

        int count = orderMapper.insertSelective(order);
        if (count != 1) {
            throw new LyException(ResponseCode.INSERT_OPERATION_FAIL);
        }

        //批量新增数据
        //提交参数如果为空，覆盖数据库默认值：创建时间更新时间
        count = detailMapper.insertDetailList(details);
        if (count != skuDTOList.size()) {
            throw new LyException(ResponseCode.INSERT_OPERATION_FAIL);
        }

        //3、保存物流订单信息
        //物流信息运单信息 根据地址ID远程调用用户微服务，获取用户地址信息
        AddressDTO addressDTO = userClient.queryAddressById(userId, orderDTO.getAddressId());
        OrderLogistics orderLogistics = BeanHelper.copyProperties(addressDTO, OrderLogistics.class);
        orderLogistics.setOrderId(orderId);
        count = orderLogisticsMapper.insertSelective(orderLogistics);
        if (count != 1) {
            throw new LyException(ResponseCode.INSERT_OPERATION_FAIL);
        }
        return orderId;
    }

    public OrderVO queryOrderById(Long orderId) {
        //根据order ID 查询订单表
        try {
            Order order = orderMapper.selectByPrimaryKey(orderId);
            OrderVO orderVO = BeanHelper.copyProperties(order, OrderVO.class);


            //查询订单项
            OrderDetail detail = new OrderDetail();
            detail.setOrderId(orderId);
            List<OrderDetail> orderDetailList = detailMapper.select(detail);
            orderVO.setDetailList(BeanHelper.copyWithCollection(orderDetailList, OrderDetailVO.class));

            //查询订单物流信息
            OrderLogistics orderLogistics = orderLogisticsMapper.selectByPrimaryKey(orderId);
            orderVO.setLogistics(BeanHelper.copyProperties(orderLogistics, OrderLogisticsVO.class));
            return orderVO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new LyException(ResponseCode.ORDER_NOT_FOUND);
        }
    }
}
