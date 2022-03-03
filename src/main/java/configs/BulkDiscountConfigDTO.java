package configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BulkDiscountConfigDTO {
    private String productCode;
    private Integer minAmount;
    private Double discountPercentage;
}
