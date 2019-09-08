package it.italiancoders.mybudgetrest.model.dto;

import java.util.Objects;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ConstraintError
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class ConstraintError  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("fieldName")
  private String fieldName;
  
  @JsonProperty("constraintsNotRespected")
  private List<String> constraintsNotRespected = null;
  public ConstraintError fieldName(String fieldName) {
    this.fieldName = fieldName;
    return this;
  }

  /**
   * the field name
   * @return fieldName
  */
  
  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public ConstraintError constraintsNotRespected(List<String> constraintsNotRespected) {
    this.constraintsNotRespected = constraintsNotRespected;
    return this;
  }

  public ConstraintError addConstraintsNotRespectedItem(String constraintsNotRespectedItem) {
    if (this.constraintsNotRespected == null) {
      this.constraintsNotRespected = new ArrayList<>();
    }
    this.constraintsNotRespected.add(constraintsNotRespectedItem);
    return this;
  }

  /**
   * a list of constraints Not Respected
   * @return constraintsNotRespected
  */
  
  public List<String> getConstraintsNotRespected() {
    return constraintsNotRespected;
  }

  public void setConstraintsNotRespected(List<String> constraintsNotRespected) {
    this.constraintsNotRespected = constraintsNotRespected;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConstraintError constraintError = (ConstraintError) o;
    return Objects.equals(this.fieldName, constraintError.fieldName) &&
        Objects.equals(this.constraintsNotRespected, constraintError.constraintsNotRespected);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fieldName, constraintsNotRespected);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConstraintError {\n");
    
    sb.append("    fieldName: ").append(toIndentedString(fieldName)).append("\n");
    sb.append("    constraintsNotRespected: ").append(toIndentedString(constraintsNotRespected)).append("\n");
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

