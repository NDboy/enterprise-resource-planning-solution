package erp;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AddressValidator implements ConstraintValidator<IsCompleteAddress, Address> {

    @Override
    public boolean isValid(Address address, ConstraintValidatorContext constraintValidatorContext) {
        return     !address.getCountry().isEmpty()
                && address.getCountry().length() <= 30
                && !address.getZipCode().isEmpty()
                && address.getZipCode().length() <= 10
                && !address.getLine().isEmpty()
                && address.getLine().length() <= 50;
    }
}
