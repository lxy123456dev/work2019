package com.leyou.user.controller;


import com.leyou.user.AddressDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressController {

    /**
     * 默认根据用户ID，地址ID 进行查询收件人地址信息
     *
     * @param userId
     * @param id
     * @return
     */
    @GetMapping("/address")
    public ResponseEntity<AddressDTO> queryAddressById(@RequestParam("userId") Long userId, @RequestParam("id") Long id) {
        AddressDTO address = new AddressDTO();
        address.setId(1L);
        address.setStreet("京顺路99号");
        address.setCity("北京");
        address.setDistrict("顺义区");
        address.setAddressee("黑马");
        address.setPhone("15800000000");
        address.setProvince("北京");
        address.setPostcode("100000");
        address.setIsDefault(true);
        return ResponseEntity.ok(address);
    }
}
