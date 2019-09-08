package it.italiancoders.mybudgetrest.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.italiancoders.mybudgetrest.model.dto.Category;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Movement
 */

@Entity
@Table(name = "MOVEMENTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(builderMethodName = "newBuilder")
public class MovementEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "ID", unique = true, columnDefinition = "serial")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "AMOUNT")
  @NotNull
  private Double amount;

  @OneToOne()
  @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID")
  private CategoryEntity category = null;

  @Column(name = "EXECUTED_AT", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  @NotNull
  private OffsetDateTime executedAt;

  @Column(name = "INSERT_AT", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private OffsetDateTime insertAt = OffsetDateTime.now();

  @Column(name = "DAY")
  @NotNull
  private Integer day;

  @Column(name = "WEEK")
  @NotNull
  private Integer week;

  @Column(name = "MONTH")
  @NotNull
  private Integer month;

  @Column(name = "YEAR")
  @NotNull
  private Integer year;

  @OneToOne()
  @JoinColumn(name = "USERNAME", referencedColumnName = "USERNAME")
  private UserEntity user = null;


}

