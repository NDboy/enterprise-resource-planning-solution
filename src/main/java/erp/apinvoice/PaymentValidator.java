package erp.apinvoice;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PaymentValidator implements ConstraintValidator<IsValidPaymentModeAndDates, PaymentModeAndDates> {

    @Override
    public boolean isValid(PaymentModeAndDates paymentModeAndDates, ConstraintValidatorContext constraintValidatorContext) {
        return paymentModeAndDates.getPaymentMode() != null
                && paymentModeAndDates.getInvoicingDate() !=null
                && paymentModeAndDates.getDueDate() != null
                && !paymentModeAndDates.getInvoicingDate().isAfter(paymentModeAndDates.getDueDate());
    }
}
