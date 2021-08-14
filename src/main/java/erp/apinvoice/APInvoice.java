package erp.apinvoice;

import erp.acounting.Accounting;
import erp.employee.Employee;
import erp.partner.Partner;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ap_invoices")
public class APInvoice {

    @Id
    @GeneratedValue(generator = "apinvoice-id-generator")
    @GenericGenerator(name = "apinvoice-id-generator",
            parameters = @Parameter(name = "prefix", value = "E21"),
            strategy = "erp.MyIdGenerator")
    private String id;

    private String invNum;

    @Embedded
    private PaymentModeAndDates paymentModeAndDates;

    @ManyToOne()
    private Partner partner;

    @Enumerated(value = EnumType.STRING)
    private InvoiceStatus invoiceStatus;

    @ElementCollection
    private List<InvoiceItem> invoiceItems;

    @OneToOne
    private Employee employee;

    @OneToMany/*(mappedBy = "apInvoice")*/
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

    public List<Accounting> getAccountings() {
        return accountings;
    }

    public void setAccountings(List<Accounting> accountings) {
        this.accountings = accountings;
    }

//    public void addAccounting(Accounting accounting) {
//        if (accountings == null) {
//            accountings = new ArrayList<>();
//        }
//        accountings.add(accounting);
//    }
}
