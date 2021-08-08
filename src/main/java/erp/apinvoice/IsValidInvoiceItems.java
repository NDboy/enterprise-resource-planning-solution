package erp.apinvoice;

import erp.AddressValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InvoiceItemValidator.class)
public @interface IsValidInvoiceItems {

    String message() default "Invoice can not be created without items";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
