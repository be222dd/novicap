import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import configs.BulkDiscountConfigDTO;
import configs.GetXForYConfigDTO;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        List<GetXForYConfigDTO> getXForYConfigDTOS = mapper.readValue(
            new File("./src/main/java/configs/files/getxfory.json"), new TypeReference<List<GetXForYConfigDTO>>() {
            });

        List<BulkDiscountConfigDTO> bulkDiscountConfigDTOS = mapper.readValue(
            new File("./src/main/java/configs/files/bulkdiscount.json"), new TypeReference<List<BulkDiscountConfigDTO>>() {
            });

        final Map<String, BulkDiscountConfigDTO> bulkDiscountConfigDTOMap = bulkDiscountConfigDTOS.stream()
            .collect(Collectors.toMap(BulkDiscountConfigDTO::getProductCode, Function.identity()));

        final Map<String, GetXForYConfigDTO> getXForYConfigDTOMap = getXForYConfigDTOS.stream()
            .collect(Collectors.toMap(GetXForYConfigDTO::getProductCode, Function.identity()));


        final Product product1 = new Product("VOUCHER", "abc", 20.0);
        final Product product2 = new Product("VOUCHER", "abc", 20.0);
        final Product product3 = new Product("VOUCHER", "abc", 20.0);
        final Product product7 = new Product("VOUCHER", "abc", 20.0);


        final Product product4 = new Product("T-SHIRT", "abc", 200.0);
        final Product product5 = new Product("T-SHIRT", "abc", 200.0);
        final Product product6 = new Product("T-SHIRT", "abc", 200.0);

        final BulkPurchaseDiscountStrategy bulkPurchaseDiscountStrategy = new BulkPurchaseDiscountStrategy(bulkDiscountConfigDTOMap);
        final GetXPayForYDiscountStrategy getXPayForYDiscountStrategy = new GetXPayForYDiscountStrategy(getXForYConfigDTOMap);

        final Checkout checkout = new Checkout(List.of(bulkPurchaseDiscountStrategy, getXPayForYDiscountStrategy));

        checkout.scan(product1);
        checkout.scan(product2);
        checkout.scan(product3);
        checkout.scan(product7);


        checkout.scan(product4);
        checkout.scan(product5);
        checkout.scan(product6);

        final double total = checkout.calculateTotal();

    }
}
