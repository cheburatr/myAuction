package com.gleb_dev.my_auction.validators;

import com.gleb_dev.my_auction.annotations.PasswordMatches;
import com.gleb_dev.my_auction.request.SignUpRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

// Validator checks if the confirm password is the same as the password
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
   @Override
   public void initialize(PasswordMatches constraint) {
   }

   @Override
   public boolean isValid(Object obj, ConstraintValidatorContext context) {
      SignUpRequest signUpRequest = (SignUpRequest) obj;
      return signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword());
   }
}
