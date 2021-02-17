package com.nizzoli.tasksManager.services;

import com.nizzoli.tasksManager.domain.User;
import com.nizzoli.tasksManager.exceptions.UsernameAlreadyExistsException;
import com.nizzoli.tasksManager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired // field injection
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){
        try {
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            // unique username
            newUser.setUsername(newUser.getUsername());
            // password and confirm password ae the same

            //we don't persist or show confirmPassword
            newUser.setPassword("");
            return userRepository.save(newUser);
        } catch (Exception e){
            throw new UsernameAlreadyExistsException("User "+ newUser.getUsername() + " existe déjà");
        }
    }
}
