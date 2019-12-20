package com.leyou.user.service;

import com.leyou.exception.LyException;
import com.leyou.exception.enums.ResponseCode;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.utils.RegexUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.leyou.constants.MQConstants.Exchange.SMS_EXCHANGE_NAME;
import static com.leyou.constants.MQConstants.RoutingKey.VERIFY_CODE_KEY;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AmqpTemplate amqpTemplate;
    private static final String KEY_PREIFX = "ly:user:register:";
    public Boolean checkData(String data, Integer type) {
        User u = new User();
        switch (type) {
            case 1:
                u.setUsername(data);
                break;
            case 2:
                u.setPhone(data);
                break;
            default:
                throw new LyException(ResponseCode.INVALID_PARAM_ERROR);
        }
        int count = userMapper.selectCount(u);
        return count == 0;
    }

    public void sendCheckCode(String phone) {
        if (!RegexUtils.isPhone(phone)) {
            throw new LyException(ResponseCode.INVALID_PHONE_NUMBER);
        }
        String code = RandomStringUtils.randomNumeric(6);
        redisTemplate.opsForValue().set(KEY_PREIFX + phone, code, 5, TimeUnit.MINUTES);
        Map<String, String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
        amqpTemplate.convertAndSend(SMS_EXCHANGE_NAME, VERIFY_CODE_KEY, msg);
    }
    public void register(User user, String code) {
        String realCheckcode = redisTemplate.opsForValue().get(KEY_PREIFX + user.getPhone());
        if (StringUtils.isNotBlank(realCheckcode) && realCheckcode.equals(code)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            int count = userMapper.insertSelective(user);
            if (count != 1) {
                throw new LyException(ResponseCode.INSERT_OPERATION_FAIL);
            }
            redisTemplate.delete(KEY_PREIFX + user.getPhone());
        } else {
            throw new LyException(ResponseCode.INVALID_PARAM_ERROR);
        }
    }
}
