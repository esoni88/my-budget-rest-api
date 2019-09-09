package it.italiancoders.mybudgetrest.model.dto;

import java.util.Objects;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.italiancoders.mybudgetrest.model.dto.Category;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Movement
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class Movement  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("id")
  private Long id;
  
  @JsonProperty("amount")
  private Double amount;
  
  @JsonProperty("category")
  private Category category = null;
  
  @JsonProperty("executedAt")
  private OffsetDateTime executedAt;
  
  @JsonProperty("note")
  private String note;
  
  public Movement id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Movement amount(Double amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
  */
  @NotNull
  
  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public Movement category(Category category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
  */
  @NotNull
  
  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public Movement executedAt(OffsetDateTime executedAt) {
    this.executedAt = executedAt;
    return this;
  }

  /**
   * Get executedAt
   * @return executedAt
  */
  
  public OffsetDateTime getExecutedAt() {
    return executedAt;
  }

  public void setExecutedAt(OffsetDateTime executedAt) {
    this.executedAt = executedAt;
  }

  public Movement note(String note) {
    this.note = note;
    return this;
  }

  /**
   * Get note
   * @return note
  */
  @Size(max=200) 
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Movement movement = (Movement) o;
    return Objects.equals(this.id, movement.id) &&
        Objects.equals(this.amount, movement.amount) &&
        Objects.equals(this.category, movement.category) &&
        Objects.equals(this.executedAt, movement.executedAt) &&
        Objects.equals(this.note, movement.note);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, amount, category, executedAt, note);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Movement {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    executedAt: ").append(toIndentedString(executedAt)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
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

