package it.italiancoders.mybudgetrest.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "REGISTRATION_TOKENS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(builderMethodName = "newBuilder")
@IdClass(RegistrationTokenEntityId.class)
public class RegistrationTokenEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "TOKEN", unique = true)
    @NotNull
    private String token;

    @Column(name = "USERNAME", length = 50, unique = true)
    @NotNull
    @Id
    private String username;

    @Column(name = "EXPIRED_AT", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime expiredAt;


    @Column(name = "CREATED_AT", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt = OffsetDateTime.now();

}
