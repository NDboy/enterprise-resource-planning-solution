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
import java.util.Objects;

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

    private double grossValue;

    public PaymentModeAndDates(PaymentMode paymentMode, LocalDate invoicingDate, LocalDate dueDate) {
        this.paymentMode = paymentMode;
        this.invoicingDate = invoicingDate;
        this.dueDate = dueDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentModeAndDates that = (PaymentModeAndDates) o;
        return paymentMode == that.paymentMode && Objects.equals(invoicingDate, that.invoicingDate) && Objects.equals(dueDate, that.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentMode, invoicingDate, dueDate);
    }
}
