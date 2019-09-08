package it.italiancoders.mybudgetrest.service.security.impl;

import it.italiancoders.mybudgetrest.dao.UserDao;
import it.italiancoders.mybudgetrest.model.entity.UserEntity;
import it.italiancoders.mybudgetrest.security.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserDao userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null /*|| user.getStatus() == null || user.getStatus() != UserEntity.UserStatusEnum.Active*/) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtUser.createIstance(user);
        }
    }
}
