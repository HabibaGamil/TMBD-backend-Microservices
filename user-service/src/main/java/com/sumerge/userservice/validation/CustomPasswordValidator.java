package com.sumerge.userservice.validation;
import java.util.Arrays;

import com.sumerge.userservice.model.dto.UserDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.passay.DigitCharacterRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.UppercaseCharacterRule;
import org.passay.WhitespaceRule;

public class CustomPasswordValidator implements ConstraintValidator<ValidPassword, Object> {

    @Override
    public void initialize(final ValidPassword arg0) {

    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        UserDto userDto = (UserDto) obj;
        if(passwordsMatch(userDto) && validPassword(userDto.getPassword())){
            return true;
        }
        return false;
    }
    public boolean validPassword(String password){
        final PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 30),
                new UppercaseCharacterRule(1),
                new DigitCharacterRule(1),
                new WhitespaceRule()
        )
        );
        final RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        return false;
    }
    public boolean passwordsMatch(UserDto userDto){
        return userDto.getPassword().equals(userDto.getMatchingPassword());

    }

}
