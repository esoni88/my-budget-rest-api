package it.italiancoders.mybudgetrest.model.dto;

import java.util.Objects;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.italiancoders.mybudgetrest.model.dto.Movement;
import it.italiancoders.mybudgetrest.model.dto.MovementListPageAllOf;
import it.italiancoders.mybudgetrest.model.dto.Page;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Paged Movement list
 */

@NoArgsConstructor
@AllArgsConstructor
public class MovementListPage  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("first")
  private Boolean first;
  
  @JsonProperty("last")
  private Boolean last;
  
  @JsonProperty("size")
  private Integer size;
  
  @JsonProperty("totalElements")
  private Integer totalElements;
  
  @JsonProperty("totalPages")
  private Integer totalPages;
  
  @JsonProperty("number")
  private Integer number;
  
  @JsonProperty("contents")
  private List<Movement> contents = new ArrayList<>();
  public MovementListPage first(Boolean first) {
    this.first = first;
    return this;
  }

  /**
   * Get first
   * @return first
  */
  @NotNull
  
  public Boolean isFirst() {
    return first;
  }

  public void setFirst(Boolean first) {
    this.first = first;
  }

  public MovementListPage last(Boolean last) {
    this.last = last;
    return this;
  }

  /**
   * Get last
   * @return last
  */
  @NotNull
  
  public Boolean isLast() {
    return last;
  }

  public void setLast(Boolean last) {
    this.last = last;
  }

  public MovementListPage size(Integer size) {
    this.size = size;
    return this;
  }

  /**
   * Get size
   * @return size
  */
  @NotNull
  
  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public MovementListPage totalElements(Integer totalElements) {
    this.totalElements = totalElements;
    return this;
  }

  /**
   * Get totalElements
   * @return totalElements
  */
  @NotNull
  
  public Integer getTotalElements() {
    return totalElements;
  }

  public void setTotalElements(Integer totalElements) {
    this.totalElements = totalElements;
  }

  public MovementListPage totalPages(Integer totalPages) {
    this.totalPages = totalPages;
    return this;
  }

  /**
   * Get totalPages
   * @return totalPages
  */
  @NotNull
  
  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public MovementListPage number(Integer number) {
    this.number = number;
    return this;
  }

  /**
   * Get number
   * @return number
  */
  @NotNull
  
  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public MovementListPage contents(List<Movement> contents) {
    this.contents = contents;
    return this;
  }

  public MovementListPage addContentsItem(Movement contentsItem) {
    this.contents.add(contentsItem);
    return this;
  }

  /**
   * Get contents
   * @return contents
  */
  @NotNull
  
  public List<Movement> getContents() {
    return contents;
  }

  public void setContents(List<Movement> contents) {
    this.contents = contents;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MovementListPage movementListPage = (MovementListPage) o;
    return Objects.equals(this.first, movementListPage.first) &&
        Objects.equals(this.last, movementListPage.last) &&
        Objects.equals(this.size, movementListPage.size) &&
        Objects.equals(this.totalElements, movementListPage.totalElements) &&
        Objects.equals(this.totalPages, movementListPage.totalPages) &&
        Objects.equals(this.number, movementListPage.number) &&
        Objects.equals(this.contents, movementListPage.contents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, last, size, totalElements, totalPages, number, contents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MovementListPage {\n");
    
    sb.append("    first: ").append(toIndentedString(first)).append("\n");
    sb.append("    last: ").append(toIndentedString(last)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    totalElements: ").append(toIndentedString(totalElements)).append("\n");
    sb.append("    totalPages: ").append(toIndentedString(totalPages)).append("\n");
    sb.append("    number: ").append(toIndentedString(number)).append("\n");
    sb.append("    contents: ").append(toIndentedString(contents)).append("\n");
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

