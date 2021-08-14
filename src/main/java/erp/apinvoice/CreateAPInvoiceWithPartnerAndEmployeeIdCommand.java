package erp.apinvoice;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class CreateAPInvoiceWithPartnerAndEmployeeIdCommand {

    @NotBlank(message = "Invoice number cannot be null or blank")
    @Length(max = 20, message = "Invoice number maximum length is 20")
    @Schema(example = "23541687298")
    private String invNum;

    @IsValidPaymentModeAndDates
    private PaymentModeAndDates paymentModeAndDates;

    @NotNull(message = "Invoice can not be created without invoice status")
    @Schema(example = "OPEN")
    private InvoiceStatus invoiceStatus;

    @IsValidInvoiceItems
    @NotNull(message = "Invoice can not be created without items")
    private List<InvoiceItem> invoiceItems;

    @Schema(example = "P-1")
    private String partnerId;

    @Schema(example = "ado")
    private String employeeId;

    public CreateAPInvoiceWithPartnerAndEmployeeIdCommand(String invNum, PaymentModeAndDates paymentModeAndDates, InvoiceStatus invoiceStatus, List<InvoiceItem> invoiceItems) {
        this.invNum = invNum;
        this.paymentModeAndDates = paymentModeAndDates;
        this.invoiceStatus = invoiceStatus;
        this.invoiceItems = invoiceItems;
    }

    public String getInvNum() {
        return invNum;
    }

    public void setInvNum(String invNum) {
        this.invNum = invNum;
    }

    public PaymentModeAndDates getPaymentModeAndDates() {
        return paymentModeAndDates;
    }

    public void setPaymentModeAndDates(PaymentModeAndDates paymentModeAndDates) {
        this.paymentModeAndDates = paymentModeAndDates;
    }

    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public List<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
