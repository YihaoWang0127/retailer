package intg;

import com.example.retailer.RetailerApplication;
import com.example.retailer.entity.Customer;
import com.example.retailer.entity.Transaction;
import com.example.retailer.repository.CustomerRepository;
import com.example.retailer.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.sql.Timestamp;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RetailerApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class RewardsRestControllerTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    public void testApplication() throws Exception {

        Customer customer = new Customer(1L,"Mike");

        Timestamp timestamp = Timestamp.valueOf("2023-06-20 10:30:30");

        Transaction transaction = new Transaction(4L,1L, timestamp,110);

        customerRepository.save(customer);

        transactionRepository.save(transaction);

        mvc.perform(get("http://localhost:8080/customer/1/rewards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"customerId\":1,\"lastMonthRewardPoints\":70,\"lastSecondMonthRewardPoints\":0,\"lastThirdMonthRewardPoints\":0,\"totalRewards\":70}"));
    }

}
