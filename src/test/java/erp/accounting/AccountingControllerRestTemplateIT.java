package erp.accounting;

import erp.general.Address;
import erp.apinvoice.*;
import erp.employee.CreateEmployeeCommand;
import erp.employee.EmployeeDTO;
import erp.employee.EmployeeStatus;
import erp.partner.CreatePartnerCommand;
import erp.partner.PartnerDTO;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {
        "delete from accountings",
        "delete from apinvoice_invoice_items",
        "delete from partner_ibans",
        "delete from ap_invoices",
        "delete from employees",
        "delete from partners"
})
public class AccountingControllerRestTemplateIT {

    @Autowired
    TestRestTemplate template;

    private PartnerDTO partnerDTO1;
    private PartnerDTO partnerDTO2;

    private EmployeeDTO employeeDTO1;
    private EmployeeDTO employeeDTO2;

    private APInvoiceDTO apInvoiceDTO1;
    private APInvoiceDTO apInvoiceDTO2;

    private Tuple tuple1;
    private Tuple tuple2;


    @BeforeEach
    void init() {
        Address address = new Address("Hungary", "H-1029", "Pasareti ut 101.");

        partnerDTO1 = template.postForObject("/api/partners", new CreatePartnerCommand("Anthony ltd.", address, "1122334455", List.of("FR123", "FR321")), PartnerDTO.class);
        String partnerId1 = partnerDTO1.getId();
        partnerDTO2 = template.postForObject("/api/partners", new CreatePartnerCommand("Jaguar ltd.", address, "9898712455", List.of("FR456", "FR654")), PartnerDTO.class);
        String partnerId2 = partnerDTO2.getId();

        employeeDTO1 = template.postForObject("/api/employees", new CreateEmployeeCommand("Anthony", "Doerr", EmployeeStatus.ACTIVE, address, LocalDate.of(2021,8,1)), EmployeeDTO.class);
        String employeeId1 = employeeDTO1.getId();
        employeeDTO2 = template.postForObject("/api/employees", new CreateEmployeeCommand("Caleb", "Carr", EmployeeStatus.ACTIVE, address, LocalDate.of(2019,1,13)), EmployeeDTO.class);
        String employeeId2 = employeeDTO2.getId();

        List<InvoiceItem> invoiceItems1 = List.of(
                new InvoiceItem("Packaging", 500.0, 15),
                new InvoiceItem("Delivering", 3200.0, 15),
                new InvoiceItem("UNIT-123", 15500.0, 15),
                new InvoiceItem("UNIT-123", 15500.0, 15),
                new InvoiceItem("UNIT-999", 77200.0, 15)
        );

        List<InvoiceItem> invoiceItems2 = List.of(
                new InvoiceItem("Packaging", 500.0, 15),
                new InvoiceItem("Delivering", 1200.0, 15),
                new InvoiceItem("UNIT-123", 15500.0, 15)
        );

        CreateAPInvoiceWithPartnerAndEmployeeIdCommand createAPInvoiceWithPartnerAndEmployeeIdCommand1 = new CreateAPInvoiceWithPartnerAndEmployeeIdCommand("11111111",
                new PaymentModeAndDates(PaymentMode.BANK_TRANSFER, LocalDate.of(2021, 8, 5), LocalDate.of(2021, 8, 6)),
                InvoiceStatus.OPEN,
                invoiceItems1,
                partnerId1,
                employeeId1);

        CreateAPInvoiceWithPartnerAndEmployeeIdCommand createAPInvoiceWithPartnerAndEmployeeIdCommand2 = new CreateAPInvoiceWithPartnerAndEmployeeIdCommand("22222222",
                new PaymentModeAndDates(PaymentMode.CASH, LocalDate.of(2021, 8, 1), LocalDate.of(2021, 8, 30)),
                InvoiceStatus.PAYED,
                invoiceItems2,
                partnerId2,
                employeeId2);

        apInvoiceDTO1 = template.postForObject("/api/apinvoices", createAPInvoiceWithPartnerAndEmployeeIdCommand1, APInvoiceDTO.class);
        apInvoiceDTO2 = template.postForObject("/api/apinvoices", createAPInvoiceWithPartnerAndEmployeeIdCommand2, APInvoiceDTO.class);

        tuple1 = tuple("ACC21-1",
                LocalDate.now(),
                "ado",
                InvoiceStatus.OPEN,
                "E21-1");

        tuple2 = tuple("ACC21-2",
                LocalDate.now(),
                "cca",
                InvoiceStatus.PAYED,
                "E21-2");
    }

    @Test
    void testCreatingAccountingWithCompleteAPInvoice() {
        List<AccountingDTO> accountingDTOS = template.exchange("/api/accountings",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<AccountingDTO>>(){})
                .getBody();

        assertThat(accountingDTOS)
                .hasSize(2)
                .extracting(AccountingDTO::getId, AccountingDTO::getAccountingDate, c -> c.getEmployee().getId(), AccountingDTO::getInvoiceStatus, e -> e.getApInvoice().getId())
                .containsExactly(tuple1, tuple2);

    }

    @Test
    void testCreatingAccountingByChangeInvoiceStatusAndFilterByInvoiceId() {
        String invoiceId1 = apInvoiceDTO1.getId();
        String employeeId2 = employeeDTO2.getId();

        template.put("/api/apinvoices/" + invoiceId1,
                new ChangeInvoiceStatusCommand(InvoiceStatus.CANCELED, employeeId2));

        List<AccountingDTO> accountingDTOS = template.exchange("/api/accountings?apInvoiceId=" + invoiceId1,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<AccountingDTO>>(){})
                .getBody();

        assertThat(accountingDTOS)
                .hasSize(2)
                .extracting(AccountingDTO::getId, AccountingDTO::getAccountingDate, c -> c.getEmployee().getId(), AccountingDTO::getInvoiceStatus, e -> e.getApInvoice().getId())
                .containsExactly(   tuple1,
                                    tuple("ACC21-3",
                                        LocalDate.now(),
                                        "cca",
                                        InvoiceStatus.CANCELED,
                                        "E21-1"));
    }

    @Test
    void testFilterByAccountingDate() {
        String invoiceId1 = apInvoiceDTO1.getId();
        String employeeId2 = employeeDTO2.getId();

        template.put("/api/apinvoices/" + invoiceId1,
                new ChangeInvoiceStatusCommand(InvoiceStatus.CANCELED, employeeId2));

        List<AccountingDTO> accountingDTOS1 = template.exchange("/api/accountings?accountingDate=" + LocalDate.now(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<AccountingDTO>>() {
                        })
                .getBody();

        assertThat(accountingDTOS1)
                .hasSize(3)
                .extracting(AccountingDTO::getId, AccountingDTO::getAccountingDate, c -> c.getEmployee().getId(), AccountingDTO::getInvoiceStatus, e -> e.getApInvoice().getId())
                .containsExactly(tuple1,
                        tuple2,
                        tuple("ACC21-3",
                                LocalDate.now(),
                                "cca",
                                InvoiceStatus.CANCELED,
                                "E21-1"));
    }

    @Test
    void testFilterByEmployeeId() {
        String invoiceId1 = apInvoiceDTO1.getId();
        String employeeId2 = employeeDTO2.getId();

        template.put("/api/apinvoices/" + invoiceId1,
                new ChangeInvoiceStatusCommand(InvoiceStatus.CANCELED, employeeId2));

        List<AccountingDTO> accountingDTOS2 = template.exchange("/api/accountings?employeeId=" + "cca",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<AccountingDTO>>() {
                        })
                .getBody();

        assertThat(accountingDTOS2)
                .hasSize(2)
                .extracting(AccountingDTO::getId, AccountingDTO::getAccountingDate, c -> c.getEmployee().getId(), AccountingDTO::getInvoiceStatus, e -> e.getApInvoice().getId())
                .containsExactly(tuple2,
                        tuple("ACC21-3",
                                LocalDate.now(),
                                "cca",
                                InvoiceStatus.CANCELED,
                                "E21-1"));
    }

    @Test
    void testFilterByEmployeeLastName() {
        String invoiceId1 = apInvoiceDTO1.getId();
        String employeeId2 = employeeDTO2.getId();

        template.put("/api/apinvoices/" + invoiceId1,
                new ChangeInvoiceStatusCommand(InvoiceStatus.CANCELED, employeeId2));

        List<AccountingDTO> accountingDTOS3 = template.exchange("/api/accountings?employeeLastName=" + "Carr",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<AccountingDTO>>(){})
                .getBody();

        assertThat(accountingDTOS3)
                .hasSize(2)
                .extracting(AccountingDTO::getId, AccountingDTO::getAccountingDate, c -> c.getEmployee().getId(), AccountingDTO::getInvoiceStatus, e -> e.getApInvoice().getId())
                .containsExactly(   tuple2,
                                    tuple("ACC21-3",
                                        LocalDate.now(),
                                        "cca",
                                        InvoiceStatus.CANCELED,
                                        "E21-1"));
    }

    @Test
    void testFilterByInvoiceStatus() {
        String invoiceId1 = apInvoiceDTO1.getId();
        String employeeId2 = employeeDTO2.getId();

        template.put("/api/apinvoices/" + invoiceId1,
                new ChangeInvoiceStatusCommand(InvoiceStatus.CANCELED, employeeId2));

        List<AccountingDTO> accountingDTOS3 = template.exchange("/api/accountings?invoiceStatus=" + InvoiceStatus.OPEN,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<AccountingDTO>>(){})
                .getBody();

        assertThat(accountingDTOS3)
                .hasSize(1)
                .extracting(AccountingDTO::getId, AccountingDTO::getAccountingDate, c -> c.getEmployee().getId(), AccountingDTO::getInvoiceStatus, e -> e.getApInvoice().getId())
                .containsExactly( tuple1);
    }


}
