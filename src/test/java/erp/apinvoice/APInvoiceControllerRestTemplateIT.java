package erp.apinvoice;

import erp.Address;
import erp.partner.CreatePartnerCommand;
import erp.partner.Partner;
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
@Sql(statements = {"delete from apinvoice_invoice_items",
                    "delete from partners_ap_invoices",
                    "delete from ap_invoices",
                    "delete from partner_ibans",
                    "delete from partners",
                    "delete from employees",
                    "delete from accountings",
                    "delete from ap_invoices_accountings"})
public class APInvoiceControllerRestTemplateIT {

    @Autowired
    TestRestTemplate template;

    private APInvoiceDTO apInvoiceDTO1;
    private APInvoiceDTO apInvoiceDTO2;

    private Tuple tuple1;
    private Tuple tuple2;

    private final String[] PARAMETERS_FOR_TUPLE_NO_INVOICEITEMS =
            new String[] {"id", "invNum", "paymentModeAndDates", "invoiceStatus"};

    private CreateAPInvoiceCommand createAPInvoiceCommand1;
    private CreateAPInvoiceCommand createAPInvoiceCommand2;

    private Address address;

    @BeforeEach
    void init() {
        List<InvoiceItem> invoiceItems1 = List.of(
                new InvoiceItem("Packaging", 500.0, 15),
                new InvoiceItem("Delivering", 3200.0, 15),
                new InvoiceItem("UNIT-123", 15500.0, 15),
                new InvoiceItem("UNIT-123", 15500.0, 15),
                new InvoiceItem("UNIT-999", 77200.0, 15)
        );
        createAPInvoiceCommand1
                = new CreateAPInvoiceCommand("11111111",
                        new PaymentModeAndDates(PaymentMode.BANK_TRANSFER, LocalDate.of(2021,8,5), LocalDate.of(2021,8,6)),
                        InvoiceStatus.OPEN,
                        invoiceItems1);

        List<InvoiceItem> invoiceItems2 = List.of(
                new InvoiceItem("Packaging", 500.0, 15),
                new InvoiceItem("Delivering", 1200.0, 15),
                new InvoiceItem("UNIT-123", 15500.0, 15)
        );
        createAPInvoiceCommand2
                = new CreateAPInvoiceCommand("22222222",
                        new PaymentModeAndDates(PaymentMode.BANK_TRANSFER, LocalDate.of(2021,8,1), LocalDate.of(2021,8,30)),
                        InvoiceStatus.OPEN,
                        invoiceItems2);

        apInvoiceDTO1 = template.postForObject("/api/apinvoices", createAPInvoiceCommand1, APInvoiceDTO.class);
        apInvoiceDTO2 = template.postForObject("/api/apinvoices", createAPInvoiceCommand2, APInvoiceDTO.class);

        tuple1 = tuple("E21-1",
                        "11111111",
                        new PaymentModeAndDates(PaymentMode.BANK_TRANSFER, LocalDate.of(2021,8,5), LocalDate.of(2021,8,6)),
                        InvoiceStatus.OPEN);

        tuple2 = tuple("E21-2",
                        "22222222",
                        new PaymentModeAndDates(PaymentMode.BANK_TRANSFER, LocalDate.of(2021,8,1), LocalDate.of(2021,8,30)),
                        InvoiceStatus.OPEN);


        address = new Address("Hungary", "H-1029", "Pasareti ut 101.");

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
                .extracting(PARAMETERS_FOR_TUPLE_NO_INVOICEITEMS)
                .containsExactly(tuple1, tuple2);

        assertThat(apInvoiceDTOS.get(0).getInvoiceItems()).hasSize(5);
        assertThat(apInvoiceDTOS.get(1).getInvoiceItems()).hasSize(3);
    }

    @Test
    void testFindInvoiceByIdAndAddPartner() {
        String invoiceId1 = apInvoiceDTO1.getId();
        String invoiceId2 = apInvoiceDTO2.getId();
        PartnerDTO partnerDTO = template.postForObject("/api/partners", new CreatePartnerCommand("Anthony ltd.", address, "1122334455"), PartnerDTO.class);
        String partnerId = partnerDTO.getId();

        template.put("/api/apinvoices/" + invoiceId1 + "/partner", new AddNewPartnerCommand(partnerId));
        template.put("/api/apinvoices/" + invoiceId2 + "/partner", new AddNewPartnerCommand(partnerId));

        List<APInvoiceDTO> apInvoiceDTOsWithPartner = template.exchange("/api/apinvoices",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<APInvoiceDTO>>(){}).getBody();

        System.out.println(apInvoiceDTOsWithPartner);

        assertThat(apInvoiceDTOsWithPartner)
                .hasSize(2)
                .extracting("partner")
                .extracting("id")
                .containsExactly("P-1", "P-1");
    }

    @Test
    void testFindInvoiceByPartnerId() {
        String invoiceId1 = apInvoiceDTO1.getId();
        String invoiceId2 = apInvoiceDTO2.getId();
        PartnerDTO partnerDTO = template.postForObject("/api/partners", new CreatePartnerCommand("Anthony ltd.", address, "1122334455"), PartnerDTO.class);

        String partnerId = partnerDTO.getId();

        template.put("/api/apinvoices/" + invoiceId1 + "/partner", new AddNewPartnerCommand(partnerId));
        template.put("/api/apinvoices/" + invoiceId2 + "/partner", new AddNewPartnerCommand(partnerId));

        List<APInvoiceDTO> apInvoiceDTOsWithPartner = template.exchange("/api/apinvoices?partnerId=" + partnerId,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<APInvoiceDTO>>(){}).getBody();

        assertThat(apInvoiceDTOsWithPartner)
                .hasSize(2)
                .extracting("id")
                .containsExactly("E21-1", "E21-2");
    }

//    @Test
//    void testChangeInvoiceStatusAndFilterByInvoiceStatus() {
//        String invoiceId1 = apInvoiceDTO1.getId();
//
//        template.put("/api/apinvoices/" + invoiceId1 + "/invoicestatus",
//                new ChangeInvoiceStatusCommand(InvoiceStatus.PAYED));
//
//        String payed = InvoiceStatus.PAYED.toString();
//        String open = InvoiceStatus.OPEN.toString();
//
//        List<APInvoiceDTO> apInvoiceDTOsPayed = template.exchange("/api/apinvoices/invoicestatus?invoiceStatus=" + payed,
//                        HttpMethod.GET,
//                        null,
//                        new ParameterizedTypeReference<List<APInvoiceDTO>>(){})
//                .getBody();
//
//        assertThat(apInvoiceDTOsPayed)
//                .hasSize(1)
//                .extracting("invoiceStatus")
//                .containsExactly(InvoiceStatus.PAYED);
//
//        List<APInvoiceDTO> apInvoiceDTOsOpen = template.exchange("/api/apinvoices/invoicestatus?invoiceStatus=" + open,
//                        HttpMethod.GET,
//                        null,
//                        new ParameterizedTypeReference<List<APInvoiceDTO>>(){})
//                .getBody();
//
//        assertThat(apInvoiceDTOsOpen)
//                .hasSize(1)
//                .extracting("invoiceStatus")
//                .containsExactly(InvoiceStatus.OPEN);
//    }

    @Test
    void testShouldThrowPartnerNotFoundException() {

        Problem result1 = template.getForObject("/api/apinvoices/xxx", Problem.class);

        assertEquals(Status.NOT_FOUND, result1.getStatus());
        assertEquals("Not found", result1.getTitle());
        assertEquals("A/P invoice not found: xxx", result1.getDetail());

    }


}
