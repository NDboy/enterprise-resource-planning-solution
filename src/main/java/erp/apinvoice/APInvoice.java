package erp.apinvoice;

import erp.accounting.Accounting;
import erp.employee.Employee;
import erp.partner.Partner;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "ap_invoices")
public class APInvoice {

    @Id
    @GeneratedValue(generator = "apinvoice-id-generator")
    @GenericGenerator(name = "apinvoice-id-generator",
            parameters = @Parameter(name = "prefix", value = "E21"),
            strategy = "erp.general.MyIdGenerator")
    private String id;

    private String invNum;

    @Embedded
    private PaymentModeAndDates paymentModeAndDates;

    @ManyToOne
    private Partner partner;

    @Enumerated(value = EnumType.STRING)
    private InvoiceStatus invoiceStatus;

    @ElementCollection
    private List<InvoiceItem> invoiceItems;

    @ManyToOne
    private Employee employee;

    @OneToMany(mappedBy = "apInvoice", orphanRemoval = true)
    private List<Accounting> accountings;

    public APInvoice(String invNum, PaymentModeAndDates paymentModeAndDates, InvoiceStatus invoiceStatus, List<InvoiceItem> invoiceItems) {
        this.invNum = invNum;
        this.paymentModeAndDates = paymentModeAndDates;
        this.invoiceStatus = invoiceStatus;
        this.invoiceItems = invoiceItems;
    }

    public APInvoice(String invNum, PaymentModeAndDates paymentModeAndDates, Partner partner, InvoiceStatus invoiceStatus, List<InvoiceItem> invoiceItems, Employee employee) {
        this.invNum = invNum;
        this.paymentModeAndDates = paymentModeAndDates;
        this.partner = partner;
        this.invoiceStatus = invoiceStatus;
        this.invoiceItems = invoiceItems;
        this.employee = employee;
    }

    public APInvoice(String id, String invNum, PaymentModeAndDates paymentModeAndDates, Partner partner, InvoiceStatus invoiceStatus, List<InvoiceItem> invoiceItems, Employee employee, List<Accounting> accountings) {
        this.id = id;
        this.invNum = invNum;
        this.paymentModeAndDates = paymentModeAndDates;
        this.partner = partner;
        this.invoiceStatus = invoiceStatus;
        this.invoiceItems = invoiceItems;
        this.employee = employee;
        this.accountings = accountings;
    }


}
