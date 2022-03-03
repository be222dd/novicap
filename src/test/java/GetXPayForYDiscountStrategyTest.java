import configs.GetXForYConfigDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class GetXPayForYDiscountStrategyTest {
    GetXPayForYDiscountStrategy strategy;

    @BeforeEach
    void setup() {
        Map<String, GetXForYConfigDTO> discountConfigs = new HashMap<>();
        discountConfigs.put("VOUCHER", new GetXForYConfigDTO("VOUCHER", 3, 1));
        strategy = new GetXPayForYDiscountStrategy(discountConfigs);
    }


    @Test
    void it_should_charge_2_for_3_items_for_given_items() {
        //given
        HashMap<String, Set<Product>> shoppingCart = new HashMap<>();

        //
        Product tshirt1 = new Product("T-SHIRT", "nike tshirt", 20);
        Product tshirt2 = new Product("T-SHIRT", "nike tshirt", 20);
        Product voucher1 = new Product("VOUCHER", "voucher", 10);
        Product voucher2 = new Product("VOUCHER", "voucher", 10);
        Product voucher3 = new Product("VOUCHER", "voucher", 10);


        final Set<Product> thisrts = Set.of(tshirt1, tshirt2);
        shoppingCart.put("T-SHIRT", thisrts);
        shoppingCart.put("VOUCHER", Set.of(voucher1, voucher2, voucher3));

        //when
        HashMap<String, Set<Product>> discountedCart = strategy.apply(shoppingCart);

        assertThat(discountedCart).isNotNull();
        assertThat(discountedCart.size()).isEqualTo(2);
        //T-shirts haven't been discounted
        assertThat(discountedCart.get("T-SHIRT"))
            .containsExactlyInAnyOrderElementsOf(thisrts);
        //One voucher has been free discounted
        assertThat(discountedCart.get("VOUCHER"))
            .extracting(Product::getPrice)
            .containsAll(List.of(0.0));
    }

    @Test
    void it_should_not_apply_discount_when_minimum_amount_not_reached() {
        //given
        HashMap<String, Set<Product>> shoppingCart = new HashMap<>();

        //
        Product tshirt1 = new Product("T-SHIRT", "nike tshirt", 20);
        Product tshirt2 = new Product("T-SHIRT", "nike tshirt", 20);
        Product voucher1 = new Product("VOUCHER", "voucher", 10);
        Product voucher2 = new Product("VOUCHER", "voucher", 10);


        final Set<Product> tshirts = Set.of(tshirt1, tshirt2);
        shoppingCart.put("T-SHIRT", tshirts);
        final Set<Product> vouchers = Set.of(voucher1, voucher2);
        shoppingCart.put("VOUCHER", vouchers);

        //when
        HashMap<String, Set<Product>> discountedCart = strategy.apply(shoppingCart);

        assertThat(discountedCart).isNotNull();
        assertThat(discountedCart.size()).isEqualTo(2);
        //T-shirts haven't been discounted
        assertThat(discountedCart.get("T-SHIRT"))
            .containsExactlyInAnyOrderElementsOf(tshirts);
        //voucher haven't been discounted
        assertThat(discountedCart.get("VOUCHER"))
            .containsExactlyInAnyOrderElementsOf(vouchers);
    }

}