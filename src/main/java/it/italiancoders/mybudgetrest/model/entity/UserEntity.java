package it.italiancoders.mybudgetrest.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(builderMethodName = "newBuilder")
public class UserEntity {
    public enum UserStatusEnum {
        Active(0),
        ToConfirm(1);

        private final int value;

        UserStatusEnum(int value) {
            this.value = value;
        }
    }
    @Id
    @Column(name = "USERNAME", length = 50, unique = true)
    @NotNull
    private String username;

    @Column(name = "PASSWORD", length = 200)
    @NotNull
    private String password;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "status")
    private UserStatusEnum status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USERS_ROLES",
            joinColumns = {@JoinColumn(name = "USER_USERNAME", referencedColumnName = "USERNAME")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ROLE_ID", referencedColumnName = "ID")})
    private List<UserRoleEntity> roles = new ArrayList<>();
}
