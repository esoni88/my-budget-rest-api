package it.italiancoders.mybudgetrest.model.dto;

import java.util.Objects;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets UserRole
 */
public enum UserRole {
  
  VIEW_BUDGET("VIEW_BUDGET"),
  
  DELETE_EXPENSE("DELETE_EXPENSE"),
  
  ADD_EXPENSE("ADD_EXPENSE");

  private String value;
  
  @JsonValue
  public String getValue() {
    return value;
  }

  UserRole(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static UserRole fromValue(String value) {
    for (UserRole b : UserRole.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

