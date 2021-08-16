package erp.apinvoice;

import erp.Address;
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
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {
        "delete from accountings",
        "delete from apinvoice_invoice_items",
        "delete from partners_ap_invoices",
        "delete from partner_ibans",
        "delete from ap_invoices",
        "delete from employees",
        "delete from partners"
                    })
public class APInvoiceControllerRestTemplateIT {

    @Autowired
    TestRestTemplate template;

    private APInvoiceDTO apInvoiceDTO1;
    private APInvoiceDTO apInvoiceDTO2;


    private PartnerDTO partnerDTO1;
    private PartnerDTO partnerDTO2;
    private PartnerDTO partnerDtoNoInvoice;

    private EmployeeDTO employeeDTO1;
    private EmployeeDTO employeeDTO2;
    private EmployeeDTO employeeDTONoInvoice;


    private Tuple tuple1;
    private Tuple tuple2;

    private final String[] PARAMETERS_FOR_TUPLE_NO_INVOICE_ITEMS =
            new String[] {"id", "invNum", "paymentModeAndDates", "invoiceStatus"};

    private CreateAPInvoiceCommand createAPInvoiceNoPE1;
    private CreateAPInvoiceCommand createAPInvoiceNoPE2;


    @BeforeEach
    void init() {
        Address address = new Address("Hungary", "H-1029", "Pasareti ut 101.");

        partnerDTO1 = template.postForObject("/api/partners", new CreatePartnerCommand("Anthony ltd.", address, "1122334455"), PartnerDTO.class);
        String partnerId1 = partnerDTO1.getId();
        partnerDTO2 = template.postForObject("/api/partners", new CreatePartnerCommand("Jaguar ltd.", address, "9898712455"), PartnerDTO.class);
        String partnerId2 = partnerDTO2.getId();
        partnerDtoNoInvoice = template.postForObject("/api/partners", new CreatePartnerCommand("Attach Later ltd.", address, "9898712455"), PartnerDTO.class);


        employeeDTO1 = template.postForObject("/api/employees", new CreateEmployeeCommand("Anthony", "Doerr", EmployeeStatus.ACTIVE, address, LocalDate.of(2021,8,1)), EmployeeDTO.class);
        String employeeId1 = employeeDTO1.getId();
        employeeDTO2 = template.postForObject("/api/employees", new CreateEmployeeCommand("Caleb", "Carr", EmployeeStatus.ACTIVE, address, LocalDate.of(2019,1,13)), EmployeeDTO.class);
        String employeeId2 = employeeDTO2.getId();
        employeeDTONoInvoice = template.postForObject("/api/employees", new CreateEmployeeCommand("Johnny", "Noinvoice", EmployeeStatus.PASSIVE, address, LocalDate.of(2011,1,13)), EmployeeDTO.class);

        List<InvoiceItem> invoiceItems1 = List.of(
                new InvoiceItem("Packaging", 500.0, 15),
                new InvoiceItem("Delivering", 3200.0, 15),
                new InvoiceItem("UNIT-123", 15500.0, 15),
                new InvoiceItem("UNIT-123", 15500.0, 15),
                new InvoiceItem("UNIT-999", 77200.0, 15)
        );

        createAPInvoiceNoPE1 = new CreateAPInvoiceCommand("11111111",
                new PaymentModeAndDates(PaymentMode.BANK_TRANSFER, LocalDate.of(2021, 8, 5), LocalDate.of(2021, 8, 6)),
                InvoiceStatus.OPEN,
                invoiceItems1);

        CreateAPInvoiceWithPartnerAndEmployeeIdCommand createAPInvoiceWithPartnerAndEmployeeIdCommand1 = new CreateAPInvoiceWithPartnerAndEmployeeIdCommand("11111111",
                new PaymentModeAndDates(PaymentMode.BANK_TRANSFER, LocalDate.of(2021, 8, 5), LocalDate.of(2021, 8, 6)),
                InvoiceStatus.OPEN,
                invoiceItems1,
                partnerId1,
                employeeId1);

        List<InvoiceItem> invoiceItems2 = List.of(
                new InvoiceItem("Packaging", 500.0, 15),
                new InvoiceItem("Delivering", 1200.0, 15),
                new InvoiceItem("UNIT-123", 15500.0, 15)
        );

        createAPInvoiceNoPE2 = new CreateAPInvoiceCommand("22222222",
                new PaymentModeAndDates(PaymentMode.BANK_TRANSFER, LocalDate.of(2021, 8, 1), LocalDate.of(2021, 8, 30)),
                InvoiceStatus.OPEN,
                invoiceItems2);

        CreateAPInvoiceWithPartnerAndEmployeeIdCommand createAPInvoiceWithPartnerAndEmployeeIdCommand2 = new CreateAPInvoiceWithPartnerAndEmployeeIdCommand("22222222",
                new PaymentModeAndDates(PaymentMode.CASH, LocalDate.of(2021, 8, 1), LocalDate.of(2021, 8, 30)),
                InvoiceStatus.PAYED,
                invoiceItems2,
                partnerId2,
                employeeId2);

        apInvoiceDTO1 = template.postForObject("/api/apinvoices", createAPInvoiceWithPartnerAndEmployeeIdCommand1, APInvoiceDTO.class);
        apInvoiceDTO2 = template.postForObject("/api/apinvoices", createAPInvoiceWithPartnerAndEmployeeIdCommand2, APInvoiceDTO.class);


        tuple1 = tuple("E21-1",
                        "11111111",
                        new PaymentModeAndDatesDTO(PaymentMode.BANK_TRANSFER, LocalDate.of(2021,8,5), LocalDate.of(2021,8,6)),
                        InvoiceStatus.OPEN);

        tuple2 = tuple("E21-2",
                        "22222222",
                        new PaymentModeAndDatesDTO(PaymentMode.CASH, LocalDate.of(2021,8,1), LocalDate.of(2021,8,30)),
                        InvoiceStatus.PAYED);
    }

    @Test
    void testCreateAndListAPInvoices() {

        List<APInvoiceDTO> apInvoiceDTOS = template.exchange("/api/apinvoices",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<APInvoiceDTO>>(){})
                .getBody();

        assertThat(apInvoiceDTOS)
                .hasSize(2)
                .extracting(PARAMETERS_FOR_TUPLE_NO_INVOICE_ITEMS)
                .containsExactly(tuple1, tuple2);

        assertThat(apInvoiceDTOS.get(0).getInvoiceItems()).hasSize(5);
        assertThat(apInvoiceDTOS.get(1).getInvoiceItems()).hasSize(3);
    }

    @Test
    void testFindInvoiceByIdAndAddPartnerAndEmployee() {
        APInvoiceDTO apInvoiceDTO3 = template.postForObject("/api/apinvoices", createAPInvoiceNoPE1, APInvoiceDTO.class);
        APInvoiceDTO apInvoiceDTO4 = template.postForObject("/api/apinvoices", createAPInvoiceNoPE2, APInvoiceDTO.class);
        String invoiceId3 = apInvoiceDTO3.getId();
        String invoiceId4 = apInvoiceDTO4.getId();

        String partnerId = partnerDtoNoInvoice.getId();
        String employeeId = employeeDTONoInvoice.getId();

        template.put("/api/apinvoices/" + invoiceId3 + "/partner", new AddNewPartnerCommand(partnerId));
        template.put("/api/apinvoices/" + invoiceId4 + "/partner", new AddNewPartnerCommand(partnerId));
        template.put("/api/apinvoices/" + invoiceId3 + "/employee", new AddNewEmployeeCommand(employeeId));
        template.put("/api/apinvoices/" + invoiceId4 + "/employee", new AddNewEmployeeCommand(employeeId));

        List<APInvoiceDTO> apInvoiceDTOsWithPartner = template.exchange("/api/apinvoices?partnerId=" + partnerId,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<APInvoiceDTO>>(){}).getBody();

        assertThat(apInvoiceDTOsWithPartner)
                .hasSize(2)
                .extracting(a -> a.getId(), b -> b.getPartner().getId(), c -> c.getEmployee().getId())
                .containsExactly(tuple(invoiceId3, partnerId, employeeId), tuple(invoiceId4, partnerId, employeeId));
    }

    @Test
    void testFindInvoiceByPartnerId() {
        String invoiceId1 = apInvoiceDTO1.getId();
        String partnerId = partnerDTO1.getId();

        List<APInvoiceDTO> apInvoiceDTOsWithPartner = template.exchange("/api/apinvoices?partnerId=" + partnerId,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<APInvoiceDTO>>(){}).getBody();

        assertThat(apInvoiceDTOsWithPartner)
                .hasSize(1)
                .extracting(a -> a.getId(), b -> b.getPartner().getId())
                .containsExactly(tuple(invoiceId1, partnerId));
    }

    @Test
    void testFindInvoiceByEmployeeId() {
        String invoiceId1 = apInvoiceDTO1.getId();
        String employeeId = employeeDTO1.getId();

        List<APInvoiceDTO> apInvoiceDTOsWithEmployee = template.exchange("/api/apinvoices?employeeId=" + employeeId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<APInvoiceDTO>>(){}).getBody();

        assertThat(apInvoiceDTOsWithEmployee)
                .hasSize(1)
                .extracting(a -> a.getId(), b -> b.getEmployee().getId())
                .containsExactly(tuple(invoiceId1, employeeId));
    }

    @Test
    void testChangeInvoiceStatusAndFilterByInvoiceStatus() {
        String invoiceId1 = apInvoiceDTO1.getId();
        String employeeId2 = employeeDTO2.getId();

        template.put("/api/apinvoices/" + invoiceId1,
                new ChangeInvoiceStatusCommand(InvoiceStatus.CANCELED, employeeId2));

        List<APInvoiceDTO> apInvoiceDTOsCanceled = template.exchange("/api/apinvoices?invoiceStatus=" + InvoiceStatus.CANCELED,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<APInvoiceDTO>>(){})
                .getBody();

        assertThat(apInvoiceDTOsCanceled)
                .hasSize(1)
                .extracting(APInvoiceDTO::getInvoiceStatus)
                .containsExactly(InvoiceStatus.CANCELED);

        List<APInvoiceDTO> apInvoiceDTOsPayed = template.exchange("/api/apinvoices?invoiceStatus=" + InvoiceStatus.PAYED,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<APInvoiceDTO>>(){})
                .getBody();

        assertThat(apInvoiceDTOsPayed)
                .hasSize(1)
                .extracting(APInvoiceDTO::getInvoiceStatus)
                .containsExactly(InvoiceStatus.PAYED);
    }

    @Test
    void testFindAPInvoiceByInvNum() {
        String invNum1 = "11111111";
        String invNum2 = "22222222";
        List<APInvoiceDTO> apInvoiceDTOsFound1 = template.exchange("/api/apinvoices?invNum=" +invNum1,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<APInvoiceDTO>>(){})
                .getBody();

        assertThat(apInvoiceDTOsFound1)
                .hasSize(1)
                .extracting(APInvoiceDTO::getInvNum)
                .containsExactly(invNum1);

        List<APInvoiceDTO> apInvoiceDTOsFound2 = template.exchange("/api/apinvoices?invNum=" +invNum2,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<APInvoiceDTO>>(){})
                .getBody();

        assertThat(apInvoiceDTOsFound2)
                .hasSize(1)
                .extracting(APInvoiceDTO::getInvNum)
                .containsExactly(invNum2);
    }

    @Test
    void testFindAPInvoiceByPaymentMode() {
        PaymentMode paymentMode1 = PaymentMode.CASH;
        PaymentMode paymentMode2 = PaymentMode.BANK_TRANSFER;
        List<APInvoiceDTO> apInvoiceDTOsFound1 = template.exchange("/api/apinvoices?paymentMode=" + paymentMode1,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<APInvoiceDTO>>(){})
                .getBody();

        assertThat(apInvoiceDTOsFound1)
                .hasSize(1)
                .extracting(a -> a.getPaymentModeAndDates().getPaymentMode())
                .containsExactly(paymentMode1);

        List<APInvoiceDTO> apInvoiceDTOsFound2 = template.exchange("/api/apinvoices?paymentMode=" + paymentMode2,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<APInvoiceDTO>>(){})
                .getBody();

        assertThat(apInvoiceDTOsFound2)
                .hasSize(1)
                .extracting(a -> a.getPaymentModeAndDates().getPaymentMode())
                .containsExactly(paymentMode2);
    }

    @Test
    void testFindAPInvoiceByDueDateIsBefore() {
        LocalDate testDueDate = LocalDate.of(2021,8,8);
        System.out.println(testDueDate);
        List<APInvoiceDTO> apInvoiceDTOsFound = template.exchange("/api/apinvoices?dueDate=" + testDueDate,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<APInvoiceDTO>>(){})
                .getBody();

        assertThat(apInvoiceDTOsFound)
                .hasSize(1)
                .extracting(a -> a.getPaymentModeAndDates().getDueDate())
                .contains(LocalDate.of(2021,8,6));
    }

    @Test
    void testFindAPInvoiceByAccountingDateIsAfter() {
        LocalDate earlierTestAccountingDate = LocalDate.now().minusDays(1);
        LocalDate laterTestAccountingDate = LocalDate.now().plusDays(1);

        List<APInvoiceDTO> apInvoiceDTOsFound1 = template.exchange("/api/apinvoices?accountingDate=" + earlierTestAccountingDate,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<APInvoiceDTO>>(){})
                .getBody();

        assertThat(apInvoiceDTOsFound1)
                .hasSize(2);

        List<APInvoiceDTO> apInvoiceDTOsFound2 = template.exchange("/api/apinvoices?accountingDate=" + laterTestAccountingDate,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<APInvoiceDTO>>(){})
                .getBody();

        assertThat(apInvoiceDTOsFound2)
                .hasSize(0);
    }

    @Test
    void testShouldThrowPartnerNotFoundException() {

        Problem result1 = template.getForObject("/api/apinvoices/xxx", Problem.class);

        assertEquals(Status.NOT_FOUND, result1.getStatus());
        assertEquals("Not found", result1.getTitle());
        assertEquals("A/P invoice not found: xxx", result1.getDetail());

    }

    @Test
    void testDeleteAPInvoice() {

        List<APInvoiceDTO> apInvoiceDTOS = template.exchange("/api/apinvoices",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<APInvoiceDTO>>(){})
                .getBody();

        assertThat(apInvoiceDTOS).hasSize(2);

        apInvoiceDTOS.forEach(a -> template.delete("/api/apinvoices/" + a.getId()));

        apInvoiceDTOS = template.exchange("/api/apinvoices",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<APInvoiceDTO>>(){})
                .getBody();

        assertThat(apInvoiceDTOS).hasSize(0);

    }


}
