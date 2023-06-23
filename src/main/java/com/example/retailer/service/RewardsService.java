package com.example.retailer.service;

import com.example.retailer.entity.Rewards;

public interface RewardsService {

    public Rewards getRewardsByCustomerId(Long customerId);

}
