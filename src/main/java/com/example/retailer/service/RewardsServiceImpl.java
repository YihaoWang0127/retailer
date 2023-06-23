package com.example.retailer.service;

import com.example.retailer.entity.Rewards;
import com.example.retailer.entity.Transaction;
import com.example.retailer.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RewardsServiceImpl implements RewardsService{

    @Autowired
    TransactionRepository transactionRepository;

    //load the strategy value from properties file
    @Value("${oneMonthDuration}")
    int oneMonthDuration;

    @Value("${firstRewardThreshold}")
    int firstRewardThreshold;

    @Value("${secondRewardThreshold}")
    int secondRewardThreshold;

    @Override
    public Rewards getRewardsByCustomerId(Long customerId) {

        //Mark one month two month and three month timestamp
        Timestamp lastMonthTimestamp = getDuration(oneMonthDuration);
        Timestamp lastSecondMonthTimestamp = getDuration(2 * oneMonthDuration);
        Timestamp lastThirdMonthTimestamp = getDuration(3 * oneMonthDuration);

        //Get all transactions based on the timestamp
        List<Transaction> lastOneMonthTransactions =
                transactionRepository.findAllByCustomerIdAndTransactionDateBetween(customerId, lastMonthTimestamp, Timestamp.from(Instant.now()));
        List<Transaction> lastTwoMonthTransactions =
                transactionRepository.findAllByCustomerIdAndTransactionDateBetween(customerId, lastSecondMonthTimestamp, lastMonthTimestamp);
        List<Transaction> lastThreeMonthTransactions =
                transactionRepository.findAllByCustomerIdAndTransactionDateBetween(customerId, lastThirdMonthTimestamp, lastSecondMonthTimestamp);

        //Calculate the rewards per month for last three month
        Long lastMonthRewardPoints = getRewards(lastOneMonthTransactions);
        Long lastSecondMonthRewardPoints = getRewards(lastTwoMonthTransactions);
        Long lastThirdMonthRewardPoints = getRewards(lastThreeMonthTransactions);

        //Construct the entity
        Rewards customerRewards = new Rewards();
        customerRewards.setCustomerId(customerId);
        customerRewards.setLastMonthRewardPoints(lastMonthRewardPoints);
        customerRewards.setLastSecondMonthRewardPoints(lastSecondMonthRewardPoints);
        customerRewards.setLastThirdMonthRewardPoints(lastThirdMonthRewardPoints);
        customerRewards.setTotalRewards(lastMonthRewardPoints + lastSecondMonthRewardPoints + lastThirdMonthRewardPoints);

        return customerRewards;

    }

    private Timestamp getDuration(int days) {

        return Timestamp.valueOf(LocalDateTime.now().minusDays(days));

    }

    private Long getRewards(List<Transaction> transactions){

        return transactions.stream().map(transaction -> calculate(transaction))
                .collect(Collectors.summingLong(r -> r.longValue()));

    }

    private Long calculate(Transaction t) {

        if (t.getTransactionAmount() > firstRewardThreshold && t.getTransactionAmount() <= secondRewardThreshold) {
            return Math.round(t.getTransactionAmount() - firstRewardThreshold);
        } else if (t.getTransactionAmount() > secondRewardThreshold) {
            return Math.round(t.getTransactionAmount() - secondRewardThreshold) * 2
                    + (secondRewardThreshold - firstRewardThreshold);
        } else {
            return 0L;
        }

    }

}