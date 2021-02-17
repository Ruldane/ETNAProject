package com.nizzoli.tasksManager.validation;

import com.nizzoli.tasksManager.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidation implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        // correct object of User
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        if (user.getPassword().length() <6){
            errors.rejectValue("password", "Length", "Le mot passe doit avoir minimum 6 caractères");
        } if (!user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "Match", "Le mot passe de confirmation doit être le même");
        }
    }
}
