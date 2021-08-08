package erp.apinvoice;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class InvoiceItemValidator implements ConstraintValidator<IsValidInvoiceItems, List> {

    @Override
    public boolean isValid(List list, ConstraintValidatorContext constraintValidatorContext) {
        return list != null
                && !list.isEmpty();
    }
}
