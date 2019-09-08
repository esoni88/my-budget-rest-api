package it.italiancoders.mybudgetrest.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BLACK_LIST_REFRESH_TOKEN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(builderMethodName = "newBuilder")
public class BlackListRefreshTokenEntity {
    @Id
    @Column(name = "REFRESHTOKEN", unique = true)
    @NotNull
    private String refreshToken;

}
