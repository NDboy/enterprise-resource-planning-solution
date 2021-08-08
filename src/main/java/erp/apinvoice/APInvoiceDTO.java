package erp.apinvoice;

import erp.employee.Employee;
import erp.partner.Partner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class APInvoiceDTO {

    private String id;

    private String invNum;

    private PaymentModeAndDates paymentModeAndDates;

    private Partner partner;

    private InvoiceStatus invoiceStatus;

    private List<InvoiceItem> invoiceItems;

    private Employee employee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
