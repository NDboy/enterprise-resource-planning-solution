package erp.apinvoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentModeAndDatesDTO {


    private PaymentMode paymentMode;

    private LocalDate invoicingDate;

    private LocalDate dueDate;

    private String iban;

    private InvoiceCurrency invoiceCurrency;

    private InvoiceValuesDTO invoiceValues;

    public PaymentModeAndDatesDTO(PaymentMode paymentMode, LocalDate invoicingDate, LocalDate dueDate) {
        this.paymentMode = paymentMode;
        this.invoicingDate = invoicingDate;
        this.dueDate = dueDate;
    }

    public PaymentModeAndDatesDTO(PaymentMode paymentMode, LocalDate invoicingDate, LocalDate dueDate, String iban) {
        this.paymentMode = paymentMode;
        this.invoicingDate = invoicingDate;
        this.dueDate = dueDate;
        this.iban = iban;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentModeAndDatesDTO that = (PaymentModeAndDatesDTO) o;
        return paymentMode == that.paymentMode && Objects.equals(invoicingDate, that.invoicingDate) && Objects.equals(dueDate, that.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentMode, invoicingDate, dueDate);
    }
}
