package it.italiancoders.mybudgetrest.model.dto;

import java.util.Objects;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.italiancoders.mybudgetrest.model.dto.ConstraintError;
import it.italiancoders.mybudgetrest.model.dto.ErrorInternal;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ErrorData
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class ErrorData  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("userTitle")
  private String userTitle;
  
  @JsonProperty("userMessage")
  private String userMessage;
  
  @JsonProperty("internal")
  private ErrorInternal internal = null;
  
  @JsonProperty("constraintErrors")
  private List<ConstraintError> constraintErrors = null;
  public ErrorData userTitle(String userTitle) {
    this.userTitle = userTitle;
    return this;
  }

  /**
   * The title for the popup error message
   * @return userTitle
  */
  @NotNull
  
  public String getUserTitle() {
    return userTitle;
  }

  public void setUserTitle(String userTitle) {
    this.userTitle = userTitle;
  }

  public ErrorData userMessage(String userMessage) {
    this.userMessage = userMessage;
    return this;
  }

  /**
   * The error message to show (depending on the locale used when creating the session)
   * @return userMessage
  */
  @NotNull
  
  public String getUserMessage() {
    return userMessage;
  }

  public void setUserMessage(String userMessage) {
    this.userMessage = userMessage;
  }

  public ErrorData internal(ErrorInternal internal) {
    this.internal = internal;
    return this;
  }

  /**
   * Get internal
   * @return internal
  */
  
  public ErrorInternal getInternal() {
    return internal;
  }

  public void setInternal(ErrorInternal internal) {
    this.internal = internal;
  }

  public ErrorData constraintErrors(List<ConstraintError> constraintErrors) {
    this.constraintErrors = constraintErrors;
    return this;
  }

  public ErrorData addConstraintErrorsItem(ConstraintError constraintErrorsItem) {
    if (this.constraintErrors == null) {
      this.constraintErrors = new ArrayList<>();
    }
    this.constraintErrors.add(constraintErrorsItem);
    return this;
  }

  /**
   * if it is valued contains a list of constraints not respected in the current post/put request
   * @return constraintErrors
  */
  
  public List<ConstraintError> getConstraintErrors() {
    return constraintErrors;
  }

  public void setConstraintErrors(List<ConstraintError> constraintErrors) {
    this.constraintErrors = constraintErrors;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorData errorData = (ErrorData) o;
    return Objects.equals(this.userTitle, errorData.userTitle) &&
        Objects.equals(this.userMessage, errorData.userMessage) &&
        Objects.equals(this.internal, errorData.internal) &&
        Objects.equals(this.constraintErrors, errorData.constraintErrors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userTitle, userMessage, internal, constraintErrors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorData {\n");
    
    sb.append("    userTitle: ").append(toIndentedString(userTitle)).append("\n");
    sb.append("    userMessage: ").append(toIndentedString(userMessage)).append("\n");
    sb.append("    internal: ").append(toIndentedString(internal)).append("\n");
    sb.append("    constraintErrors: ").append(toIndentedString(constraintErrors)).append("\n");
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

