package erp.general;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AddressValidator implements ConstraintValidator<IsCompleteAddress, Address> {

    @Override
    public boolean isValid(Address address, ConstraintValidatorContext constraintValidatorContext) {
        return     !address.getCountry().isBlank()
                && address.getCountry().length() <= 30
                && !address.getZipCode().isBlank()
                && address.getZipCode().length() <= 10
                && !address.getLine().isBlank()
                && address.getLine().length() <= 50;
    }
}
