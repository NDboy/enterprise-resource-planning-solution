package erp.acounting;

import erp.apinvoice.APInvoice;
import erp.apinvoice.InvoiceStatus;
import erp.employee.Employee;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
public class AccountingDTO {

    private String id;

    private LocalDate accountingDate;

    private Employee employee;

    private InvoiceStatus invoiceStatus;

    private APInvoice apInvoice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getAccountingDate() {
        return accountingDate;
    }

    public void setAccountingDate(LocalDate accountingDate) {
        this.accountingDate = accountingDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public APInvoice getApInvoice() {
        return apInvoice;
    }

    public void setApInvoice(APInvoice apInvoice) {
        this.apInvoice = apInvoice;
    }
}
