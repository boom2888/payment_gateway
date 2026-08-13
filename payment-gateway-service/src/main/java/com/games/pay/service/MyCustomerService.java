package com.games.pay.service;

import com.games.pay.dto.CustomerWithUserDto;
import com.games.payment.mapper.CustomerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class MyCustomerService {

    @Resource
    private CustomerMapper customerMapper;

    public List<CustomerWithUserDto> selectCustomerWithUser(){
        return customerMapper.selectCustomerWithUser();
    }
}
