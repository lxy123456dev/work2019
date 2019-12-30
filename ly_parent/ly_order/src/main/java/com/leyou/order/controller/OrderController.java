package com.leyou.order.controller;

import com.leyou.order.dto.OrderDTO;
import com.leyou.order.service.OrderService;
import com.leyou.order.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 保存订单
     *
     * @param orderDTO:包含：收货地址ID，购物车商品数据（skuID, num），支付类型
     * @return 订单ID，提交订单成功了，进入支付页面，需要根据订单ID生成支付二维码
     */
    @PostMapping("/order")
    public ResponseEntity<Long> saveOrder(@RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.createOrder(orderDTO));
    }

    /**
     * 根据订单ID 查询订单信息
     *
     * @param orderId
     * @return
     */
    @GetMapping("/order/{id}")
    public ResponseEntity<OrderVO> queryOrderById(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(orderService.queryOrderById(orderId));
    }
}
