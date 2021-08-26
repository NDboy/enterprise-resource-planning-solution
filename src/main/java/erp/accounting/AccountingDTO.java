package erp.accounting;

import erp.apinvoice.APInvoiceDTO;
import erp.apinvoice.InvoiceStatus;
import erp.employee.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountingDTO {

    private String id;

    private LocalDate accountingDate;

    private EmployeeDTO employee;

    private InvoiceStatus invoiceStatus;

    private APInvoiceDTO apInvoice;

}
