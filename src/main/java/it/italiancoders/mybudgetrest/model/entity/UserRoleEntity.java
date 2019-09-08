package it.italiancoders.mybudgetrest.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Table(name = "USER_ROLES")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class UserRoleEntity {
    @Id
    @Column(name = "ID")
    private String id;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private List<UserEntity> users;

}
