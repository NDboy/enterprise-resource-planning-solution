package erp.apinvoice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentModeAndDates {

    @NotBlank(message = "Payment mode cannot be null or blank")
    @Schema(example = "BANK_TRANSFER")
    @Enumerated(value = EnumType.STRING)
    private PaymentMode paymentMode;

    @NotBlank(message = "Invoicing date cannot be null or blank")
    @Schema(example = "2021-08-06")
    private LocalDate invoicingDate;

    @NotBlank(message = "Due date cannot be null or blank")
    @Schema(example = "2021-08-06")
    private LocalDate dueDate;
}
