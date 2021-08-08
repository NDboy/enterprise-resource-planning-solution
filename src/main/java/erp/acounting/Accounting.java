package erp.acounting;

import erp.apinvoice.APInvoice;
import erp.apinvoice.InvoiceStatus;
import erp.employee.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accountings")
@Data
public class Accounting {

    @Id
    @GeneratedValue(generator = "accounting-id-generator")
    @GenericGenerator(name = "accounting-id-generator",
            parameters = @Parameter(name = "prefix", value = "ACC21"),
            strategy = "erp.MyIdGenerator")
    private String id;

    private LocalDate accountingDate;

    @OneToOne
    private Employee employee;

    @Enumerated(value = EnumType.STRING)
    private InvoiceStatus invoiceStatus;

    @ManyToOne
    private APInvoice apInvoice;

    public Accounting(LocalDate accountingDate, Employee employee, InvoiceStatus invoiceStatus, APInvoice apInvoice) {
        this.accountingDate = accountingDate;
        this.employee = employee;
        this.invoiceStatus = invoiceStatus;
        this.apInvoice = apInvoice;
    }
}
