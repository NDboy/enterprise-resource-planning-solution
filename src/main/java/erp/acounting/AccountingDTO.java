package erp.acounting;

import com.fasterxml.jackson.annotation.JsonBackReference;
import erp.apinvoice.APInvoice;
import erp.apinvoice.APInvoiceDTO;
import erp.apinvoice.InvoiceStatus;
import erp.employee.Employee;
import erp.employee.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
public class AccountingDTO {

    private String id;

    private LocalDate accountingDate;

    private EmployeeDTO employee;

    private InvoiceStatus invoiceStatus;

    private APInvoiceDTO apInvoice;

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

    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    public APInvoiceDTO getApInvoice() {
        return apInvoice;
    }

    public void setApInvoice(APInvoiceDTO apInvoice) {
        this.apInvoice = apInvoice;
    }
}
