package erp;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AddressValidator.class)
public @interface IsCompleteAddress {

    String message() default "Address parameters cannot be null or blank! Country max length is 30, zip code max length is 10 and address line max length is 50 characters";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
