package com.example.retailer.rewardsController;

import com.example.retailer.entity.Customer;
import com.example.retailer.entity.Rewards;
import com.example.retailer.exception.CustomerNotFoundException;
import com.example.retailer.repository.CustomerRepository;
import com.example.retailer.service.RewardsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class RewardsRestController {

    private static final Logger logger = LogManager.getLogger(RewardsRestController.class);

    @Autowired
    RewardsService rewardsService;

    @Autowired
    CustomerRepository customerRepository;

    @GetMapping(value = "/{customerId}/rewards")
    public ResponseEntity<Rewards> getRewardsByCustomerId(@PathVariable("customerId") Long customerId){

        Customer customer = customerRepository.findByCustomerId(customerId);

        // Handle exception
        if(customer == null){
            throw new CustomerNotFoundException("Customer id is not found - " + customerId);
        }

        Rewards customerRewards = rewardsService.getRewardsByCustomerId(customerId);

        return new ResponseEntity<>(customerRewards, HttpStatus.OK);

    }

}
