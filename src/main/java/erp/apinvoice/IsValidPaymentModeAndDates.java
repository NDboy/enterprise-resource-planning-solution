package erp.apinvoice;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PaymentValidator.class)
public @interface IsValidPaymentModeAndDates {

    String message() default "Payment parameters cannot be null or blank! The due date can not be previous than invoicing date";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
