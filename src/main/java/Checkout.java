import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Checkout {

    private HashMap<String, Set<Product>> shoppingCart = new HashMap<>();

    private List<DiscountStrategy> discountStrategies;

    public Checkout(List<DiscountStrategy> discountStrategies) {
        this.discountStrategies = discountStrategies;
    }

    public void scan(Product product) {
        final String productCode = product.getCode();
        final Set<Product> addedProducts = shoppingCart.getOrDefault(productCode, new HashSet<Product>());

        addedProducts.add(product);
        shoppingCart.put(productCode, addedProducts);
    }

    private HashMap<String, Set<Product>> getDiscountedShoppingCart() {
        HashMap<String, Set<Product>> shoppingCartWithDiscount = this.shoppingCart;
        for (DiscountStrategy discountStrategy : discountStrategies) {
            discountStrategy.apply(shoppingCartWithDiscount);
        }
        return shoppingCartWithDiscount;
    }


    public double calculateTotal() {
        final HashMap<String, Set<Product>> discountedShoppingCart = getDiscountedShoppingCart();
        return discountedShoppingCart.values()
            .stream()
            .map(list -> list.stream().mapToDouble(Product::getPrice).sum())
            .mapToDouble(Double::doubleValue)
            .sum();
    }
}
