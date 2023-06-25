package unit;


import com.example.retailer.RetailerApplication;
import com.example.retailer.entity.Customer;
import com.example.retailer.entity.Transaction;
import com.example.retailer.repository.CustomerRepository;
import com.example.retailer.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = RetailerApplication.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(
        locations = "classpath:application-test.properties")
@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void testApplication_customerRepository(){

        // given
        Customer jase = new Customer("Jase");
        entityManager.persist(jase);
        entityManager.flush();

        // when
        Customer found = customerRepository.findByCustomerId(1L);

        // then
        assertThat(found.getCustomerName())
                .isEqualTo(jase.getCustomerName());

    }

    //customer with multiple transactions
    @Test
    public void testApplication_transactionRepository_multiTransactions(){

        // initialize transaction
        Customer customer = new Customer(1L,"Mike");
        Timestamp timestamp1 = Timestamp.valueOf("2023-06-20 10:30:30");
        Timestamp timestamp2 = Timestamp.valueOf("2023-06-24 10:30:30");
        Transaction transaction1 = new Transaction(3L,1L, timestamp1,110);
        Transaction transaction2 = new Transaction(4L,1L, timestamp2,70);
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        //narrow timestamp
        Timestamp lastMonthTimestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(30));

        // when
        List<Transaction> transactions =
                transactionRepository.findAllByCustomerIdAndTransactionDateBetween(1L, lastMonthTimestamp, Timestamp.from(Instant.now()));

        // then
        assertThat(transactions.get(0).getTransactionAmount()).isEqualTo(110);
        assertThat(transactions.get(0).getCustomerId()).isEqualTo(1L);
        assertThat(transactions.get(1).getTransactionAmount()).isEqualTo(70);
        assertThat(transactions.get(1).getCustomerId()).isEqualTo(1L);

    }

    // customer transacitons during different month
    @Test
    public void testApplication_transactionRepository_multiMonths(){

        // initialize transaction
        Customer customer = new Customer(1L,"Mike");
        Timestamp timestamp1 = Timestamp.valueOf("2023-06-20 10:30:30");
        Timestamp timestamp2 = Timestamp.valueOf("2023-05-24 10:30:30");
        Transaction transaction1 = new Transaction(3L,1L, timestamp1,110);
        Transaction transaction2 = new Transaction(4L,1L, timestamp2,70);
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        //narrow timestamp
        Timestamp lastMonthTimestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
        Timestamp lastSecondMonthTimestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(60));

        // when
        List<Transaction> lastOneMonthTransactions =
                transactionRepository.findAllByCustomerIdAndTransactionDateBetween(1L, lastMonthTimestamp, Timestamp.from(Instant.now()));

        List<Transaction> lastTwoMonthTransactions =
                transactionRepository.findAllByCustomerIdAndTransactionDateBetween(1L, lastSecondMonthTimestamp, lastMonthTimestamp);

        // then
        assertThat(lastOneMonthTransactions.get(0).getTransactionAmount()).isEqualTo(110);
        assertThat(lastOneMonthTransactions.get(0).getCustomerId()).isEqualTo(1L);
        assertThat(lastTwoMonthTransactions.get(0).getTransactionAmount()).isEqualTo(70);
        assertThat(lastTwoMonthTransactions.get(0).getCustomerId()).isEqualTo(1L);

    }

}
