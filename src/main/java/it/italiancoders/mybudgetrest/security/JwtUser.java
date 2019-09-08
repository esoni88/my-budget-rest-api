package it.italiancoders.mybudgetrest.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.italiancoders.mybudgetrest.model.dto.User;
import it.italiancoders.mybudgetrest.model.dto.UserRole;
import it.italiancoders.mybudgetrest.model.entity.UserEntity;
import it.italiancoders.mybudgetrest.model.entity.UserRoleEntity;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JwtUser extends User implements UserDetails {
    @JsonIgnore
    private String password;

    @Builder(builderMethodName = "newBuilderExt")
    public JwtUser(String username, String email, List<UserRole> roles, String password) {
        super(username, email, roles);
        this.password = password;
    }

    public static JwtUser createIstance(UserEntity user) {
        return  JwtUser.newBuilderExt()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles() == null ? null
                        : user.getRoles().stream().map((v) -> UserRole.fromValue(v.getId())).collect(Collectors.toList()))
                .build();
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<UserRole> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles() == null ? null : mapToGrantedAuthorities(this.getRoles());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
