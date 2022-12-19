package com.gleb_dev.my_auction.validators;

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailValidatorTest {

    EmailValidator emailValidator = new EmailValidator();

    @Test
    public void isValid_ValidEmail_ShouldReturnTrue(){
        Assertions.assertTrue(emailValidator.isValid("email@mail.com", null));
    }

    @Test
    public void isValid_InvalidEmail_ShouldReturnFalse(){
        Assertions.assertFalse(emailValidator.isValid("a@.", null));
    }
}
