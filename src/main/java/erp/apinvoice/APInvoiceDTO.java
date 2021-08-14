package erp.apinvoice;

import erp.employee.EmployeeDTO;
import erp.partner.PartnerDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class APInvoiceDTO {

    private String id;

    private String invNum;

    private PaymentModeAndDatesDTO paymentModeAndDates;

    private PartnerDTO partner;

    private InvoiceStatus invoiceStatus;

    private List<InvoiceItemDTO> invoiceItems;

    private EmployeeDTO employee;

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

    public PaymentModeAndDatesDTO getPaymentModeAndDates() {
        return paymentModeAndDates;
    }

    public void setPaymentModeAndDates(PaymentModeAndDatesDTO paymentModeAndDates) {
        this.paymentModeAndDates = paymentModeAndDates;
    }

    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public List<InvoiceItemDTO> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItemDTO> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public PartnerDTO getPartner() {
        return partner;
    }

    public void setPartner(PartnerDTO partner) {
        this.partner = partner;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }
}
