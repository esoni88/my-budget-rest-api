package it.italiancoders.mybudgetrest.model.entity;

import lombok.*;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "CATEGORIES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(builderMethodName = "newBuilder")
public class CategoryEntity {
    @Id
    @Column(name = "ID", unique = true, columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", length = 100, unique = true)
    @NotNull
    private String name;

    @Column(name = "USERNAME", length = 100)
    @NotNull
    private String username;

    @Column(name = "DESCRIPTION", length = 300)
    private String description;

    @Column(name = "IS_READONLY")
    private Boolean isReadOnly;

}
