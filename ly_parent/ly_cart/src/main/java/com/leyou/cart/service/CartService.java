package com.leyou.cart.service;

import com.leyou.cart.entity.Cart;
import com.leyou.exception.LyException;
import com.leyou.exception.enums.ResponseCode;
import com.leyou.threadlocals.UserHolder;
import com.leyou.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "ly:cart:uid:";

    public void addCart(Cart cart) {
        String key = KEY_PREFIX + UserHolder.getUser();
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
        String skuId = cart.getSkuId().toString();
        int num = cart.getNum();
        // 判断要添加的商品是否存在
        Boolean boo = hashOps.hasKey(skuId);
        if (boo != null && boo) {
            // 存在，修改数量
            cart = JsonUtils.toBean(hashOps.get(skuId), Cart.class);
            if (cart != null) {
                cart.setNum(num + cart.getNum());
            }
        }
        // 写入redis
        hashOps.put(skuId, Objects.requireNonNull(JsonUtils.toString(cart)));
    }

    public List<Cart> queryCartList() {
        // 获取登录用户
        String key = KEY_PREFIX + UserHolder.getUser();
        // 判断是否存在购物车
        Boolean boo = this.redisTemplate.hasKey(key);
        if (boo == null || !boo) {
            // 不存在，直接返回
            throw new LyException(ResponseCode.CARTS_NOT_FOUND);
        }
        BoundHashOperations<String, String, String> hashOps = this.redisTemplate.boundHashOps(key);
        // 判断是否有数据
        Long size = hashOps.size();
        if (size == null || size < 0) {
            // 不存在，直接返回
            throw new LyException(ResponseCode.CARTS_NOT_FOUND);
        }
        List<String> carts = hashOps.values();
        // 查询购物车数据
        return Objects.requireNonNull(carts).stream()
                .map(json -> JsonUtils.toBean(json, Cart.class))
                .collect(Collectors.toList());
    }

    public void updateNum(Long skuId, Integer num) {
        // 获取当前用户
        Long userId = UserHolder.getUser();
        String key = KEY_PREFIX + userId;

        // 获取hash操作的对象
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
        String hashKey = skuId.toString();
        Boolean boo = hashOps.hasKey(hashKey);
        if (boo == null || !boo) {
            log.error("购物车商品不存在，用户：{}, 商品：{}", userId, skuId);
            throw new LyException(ResponseCode.CARTS_NOT_FOUND);
        }
        // 查询购物车商品
        Cart c = JsonUtils.toBean(hashOps.get(hashKey), Cart.class);
        // 修改数量
        Objects.requireNonNull(c).setNum(num);

        // 写回redis
        hashOps.put(hashKey, JsonUtils.toString(c));
    }

    public void deleteCart(String skuId) {
        // 获取登录用户
        Long userId = UserHolder.getUser();
        String key = KEY_PREFIX + userId;
        this.redisTemplate.opsForHash().delete(key, skuId);
    }

    public void addCartList(List<Cart> cartList) {
        // 获取当前用户
        String key = KEY_PREFIX + UserHolder.getUser();

        // 获取hash操作的对象，并绑定用户id
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);

        for (Cart cart : cartList) {
            addCartInRedis(cart, hashOps);
        }
    }

    private void addCartInRedis(Cart cart, BoundHashOperations<String, String, String> hashOps) {
        // 获取商品id，作为hashKey
        String hashKey = cart.getSkuId().toString();
        // 获取数量
        int num = cart.getNum();
        // 判断要添加的商品是否存在
        Boolean boo = hashOps.hasKey(hashKey);
        if (boo != null && boo) {
            // 存在，修改数量
            cart = JsonUtils.toBean(hashOps.get(hashKey), Cart.class);
            cart.setNum(num + cart.getNum());
        }
        // 写入redis
        hashOps.put(hashKey, JsonUtils.toString(cart));
    }
}

