package it.italiancoders.mybudgetrest.model.dto;

import java.util.Objects;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.italiancoders.mybudgetrest.model.dto.CategoryMovementOverview;
import it.italiancoders.mybudgetrest.model.dto.MovementListPage;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ExpenseSummary
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class ExpenseSummary  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("totalAmount")
  private Double totalAmount;
  
  @JsonProperty("categoryOverview")
  private List<CategoryMovementOverview> categoryOverview = null;
  @JsonProperty("lastMovements")
  private MovementListPage lastMovements = null;
  
  public ExpenseSummary totalAmount(Double totalAmount) {
    this.totalAmount = totalAmount;
    return this;
  }

  /**
   * Get totalAmount
   * @return totalAmount
  */
  
  public Double getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(Double totalAmount) {
    this.totalAmount = totalAmount;
  }

  public ExpenseSummary categoryOverview(List<CategoryMovementOverview> categoryOverview) {
    this.categoryOverview = categoryOverview;
    return this;
  }

  public ExpenseSummary addCategoryOverviewItem(CategoryMovementOverview categoryOverviewItem) {
    if (this.categoryOverview == null) {
      this.categoryOverview = new ArrayList<>();
    }
    this.categoryOverview.add(categoryOverviewItem);
    return this;
  }

  /**
   * Get categoryOverview
   * @return categoryOverview
  */
  
  public List<CategoryMovementOverview> getCategoryOverview() {
    return categoryOverview;
  }

  public void setCategoryOverview(List<CategoryMovementOverview> categoryOverview) {
    this.categoryOverview = categoryOverview;
  }

  public ExpenseSummary lastMovements(MovementListPage lastMovements) {
    this.lastMovements = lastMovements;
    return this;
  }

  /**
   * Get lastMovements
   * @return lastMovements
  */
  
  public MovementListPage getLastMovements() {
    return lastMovements;
  }

  public void setLastMovements(MovementListPage lastMovements) {
    this.lastMovements = lastMovements;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExpenseSummary expenseSummary = (ExpenseSummary) o;
    return Objects.equals(this.totalAmount, expenseSummary.totalAmount) &&
        Objects.equals(this.categoryOverview, expenseSummary.categoryOverview) &&
        Objects.equals(this.lastMovements, expenseSummary.lastMovements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalAmount, categoryOverview, lastMovements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExpenseSummary {\n");
    
    sb.append("    totalAmount: ").append(toIndentedString(totalAmount)).append("\n");
    sb.append("    categoryOverview: ").append(toIndentedString(categoryOverview)).append("\n");
    sb.append("    lastMovements: ").append(toIndentedString(lastMovements)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

