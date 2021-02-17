package com.nizzoli.tasksManager.services;

import com.nizzoli.tasksManager.domain.User;
import com.nizzoli.tasksManager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(name);
        if (user==null){
            new UsernameNotFoundException("L'utilisateur n'a pas été trouvé");
        }
        return user;
    }

    @Transactional
    public User loadUserById(Long id){
        User user = userRepository.getById(id);
        if (user==null){
            new UsernameNotFoundException("L'utilisateur n'a pas été trouvé");
        }
        return user;
    }
}
