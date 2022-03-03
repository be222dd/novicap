import configs.BulkDiscountConfigDTO;
import configs.GetXForYConfigDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CheckoutIntegrationTest {
    private Checkout checkout;


    @BeforeEach
    void setup() {
        Map<String, GetXForYConfigDTO> xForYDiscountConfigs = new HashMap<>();
        xForYDiscountConfigs.put("VOUCHER", new GetXForYConfigDTO("VOUCHER", 3, 1));

        Map<String, BulkDiscountConfigDTO> bulkDiscountConfigs = new HashMap<>();
        bulkDiscountConfigs.put("MUG", new BulkDiscountConfigDTO("MUG", 2, 10.0));

        GetXPayForYDiscountStrategy getXPayForYDiscountStrategy = new GetXPayForYDiscountStrategy(xForYDiscountConfigs);
        BulkPurchaseDiscountStrategy bulkPurchaseDiscountStrategy = new BulkPurchaseDiscountStrategy(bulkDiscountConfigs);

        checkout = new Checkout(List.of(getXPayForYDiscountStrategy, bulkPurchaseDiscountStrategy));
    }

    @Test
    void it_should_apply_both_discount_in_given_categories() {
        Product product1 = new Product("VOUCHER", "name1", 10.0);
        Product product2 = new Product("VOUCHER", "name2", 10.0);
        Product product3 = new Product("VOUCHER", "name3", 10.0);
        Product product4 = new Product("VOUCHER", "name4", 10.0);
        Product product5 = new Product("MUG", "name5", 20.0);
        Product product6 = new Product("MUG", "name6", 20.0);

        checkout.scan(product1);
        checkout.scan(product2);
        checkout.scan(product3);
        checkout.scan(product4);
        checkout.scan(product5);
        checkout.scan(product6);

        // %20 applied for mug 2 mug = 36
        // 1 free for every 3  applied for voucher 4 voucher cost = 3
        //total = 66
        final double expected = checkout.calculateTotal();

        assertThat(expected).isEqualTo(66.0);
    }

    @Test
    void it_should_not_apply_both_discount_in_given_categories() {
        Product product1 = new Product("VOUCHER", "name1", 10.0);
        Product product4 = new Product("VOUCHER", "name4", 10.0);
        Product product5 = new Product("MUG", "name5", 20.0);
        Product product6 = new Product("T-SHIRT", "name6", 90.0);


        checkout.scan(product1);
        checkout.scan(product4);
        checkout.scan(product5);
        checkout.scan(product6);


        // no discount applied COST 130
        final double expected = checkout.calculateTotal();

        assertThat(expected).isEqualTo(130.0);
    }

    @Test
    void it_should_apply_2_free_for_6_vouchers() {
        Product product1 = new Product("VOUCHER", "name1", 10.0);
        Product product2 = new Product("VOUCHER", "name2", 10.0);
        Product product3 = new Product("VOUCHER", "name3", 10.0);
        Product product4 = new Product("VOUCHER", "name4", 10.0);
        Product product5 = new Product("VOUCHER", "name5", 10.0);
        Product product6 = new Product("VOUCHER", "name6", 10.0);

        checkout.scan(product1);
        checkout.scan(product2);
        checkout.scan(product3);
        checkout.scan(product4);
        checkout.scan(product5);
        checkout.scan(product6);

        // 1 free for every 3  applied for voucher 6 voucher cost = 40
        //total = 66
        final double expected = checkout.calculateTotal();

        assertThat(expected).isEqualTo(40.0);
    }


}