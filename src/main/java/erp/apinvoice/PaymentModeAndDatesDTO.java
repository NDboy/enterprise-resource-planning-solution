package erp.apinvoice;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class PaymentModeAndDatesDTO {


    private PaymentMode paymentMode;

    private LocalDate invoicingDate;

    private LocalDate dueDate;

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public LocalDate getInvoicingDate() {
        return invoicingDate;
    }

    public void setInvoicingDate(LocalDate invoicingDate) {
        this.invoicingDate = invoicingDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
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
