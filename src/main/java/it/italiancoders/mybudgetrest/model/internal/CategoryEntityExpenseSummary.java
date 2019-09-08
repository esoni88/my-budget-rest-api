package it.italiancoders.mybudgetrest.model.internal;

import it.italiancoders.mybudgetrest.model.entity.CategoryEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
@Getter
@Setter
public class CategoryEntityExpenseSummary {
    private CategoryEntity category;
    private Double totalAmount;
}
