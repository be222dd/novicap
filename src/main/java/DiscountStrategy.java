import java.util.HashMap;
import java.util.Set;

public interface DiscountStrategy {
    HashMap<String, Set<Product>> apply(HashMap<String, Set<Product>> shoppingCart);
}
