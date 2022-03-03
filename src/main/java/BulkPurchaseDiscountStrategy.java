import configs.BulkDiscountConfigDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class BulkPurchaseDiscountStrategy implements DiscountStrategy {

    private final Map<String, BulkDiscountConfigDTO> discountConfigs;


    public BulkPurchaseDiscountStrategy(Map<String, BulkDiscountConfigDTO> discountConfigs) {
        this.discountConfigs = discountConfigs;
    }


    @Override
    public HashMap<String, Set<Product>> apply(HashMap<String, Set<Product>> shoppingCart) {


        shoppingCart.forEach((productCode, products) -> {
            final BulkDiscountConfigDTO productDiscountConfig = discountConfigs.get(productCode);

            if (Objects.isNull(productDiscountConfig)) {
                return;
            }
            final Integer minimumAmount = productDiscountConfig.getMinAmount();

            if (minimumAmount <= products.size()) {
                products.forEach(product -> product.setPrice(product.getPrice() / 100 * (100 - productDiscountConfig.getDiscountPercentage())));
            }

        });

        return shoppingCart;
    }
}
