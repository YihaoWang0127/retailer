package unit;


import com.example.retailer.RetailerApplication;
import com.example.retailer.entity.Customer;
import com.example.retailer.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Test
    public void testApplication(){

        // given
        Customer alex = new Customer("Alex");
        entityManager.persist(alex);
        entityManager.flush();

        // when
        Customer found = customerRepository.findByCustomerId(1L);

        // then
        assertThat(found.getCustomerName())
                .isEqualTo(alex.getCustomerName());

    }

}
