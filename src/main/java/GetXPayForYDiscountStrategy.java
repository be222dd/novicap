import configs.GetXForYConfigDTO;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class GetXPayForYDiscountStrategy implements DiscountStrategy {

    private final Map<String, GetXForYConfigDTO> discountConfigs;

    public GetXPayForYDiscountStrategy(Map<String, GetXForYConfigDTO> discountConfigs) {
        this.discountConfigs = discountConfigs;
    }

    @Override
    public HashMap<String, Set<Product>> apply(HashMap<String, Set<Product>> shoppingCart) {

        shoppingCart.forEach((productCode, products) -> {
            final GetXForYConfigDTO productDiscountConfig = discountConfigs.get(productCode);

            if (Objects.isNull(productDiscountConfig)) {
                return;
            }

            final Integer minimumAmount = productDiscountConfig.getMinAmount();
            int freeProductCount = (products.size() / minimumAmount) * productDiscountConfig.getFreeAmount();

            final Iterator<Product> productIterator = products.iterator();

            while (productIterator.hasNext() && freeProductCount > 0) {
                productIterator.next().setPrice(0.0);
                freeProductCount--;
            }
        });
        return shoppingCart;
    }

}
