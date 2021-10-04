package erp.partner;

import erp.general.Address;
import erp.general.AddressDTO;
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

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {
        "delete from accountings",
        "delete from apinvoice_invoice_items",
        "delete from partner_ibans",
        "delete from ap_invoices",
        "delete from employees",
        "delete from partners"
})
public class PartnerControllerRestTemplateIT {

    @Autowired
    TestRestTemplate template;

    private CreatePartnerCommand createPartnerCommand1;
    private CreatePartnerCommand createPartnerCommand2;
    private Tuple tuple1;
    private Tuple tuple2;

    private final String[] PARAMETERS_FOR_TUPLE_NO_IBANS = new String[] {"id", "name", "address", "taxNo"};

    private Address address;
    private AddressDTO addressDTO;

    @BeforeEach
    void init() {
        address = new Address("Hungary", "H-1029", "Pasareti ut 101.");
        addressDTO = new AddressDTO("Hungary", "H-1029", "Pasareti ut 101.");

        createPartnerCommand1 = new CreatePartnerCommand("Anthony Company ltd.", address, "123456789", List.of("FR123", "FR321"));
        tuple1 = tuple("P-1", "Anthony Company ltd.", addressDTO, "123456789");

        createPartnerCommand2 = new CreatePartnerCommand("Doerr Company ltd.", address, "987654321", List.of("FR456", "FR654"));
        tuple2 = tuple("P-2", "Doerr Company ltd.", addressDTO, "987654321");

    }

    @Test
    void testCreateAndListPartners() {
        template.postForObject("/api/partners", createPartnerCommand1, PartnerDTO.class);
        template.postForObject("/api/partners", createPartnerCommand2, PartnerDTO.class);

        List<PartnerDTO> partnerDTOS = template.exchange("/api/partners",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<PartnerDTO>>(){})
                .getBody();

        assertThat(partnerDTOS)
                .hasSize(2)
                .extracting(PARAMETERS_FOR_TUPLE_NO_IBANS)
                .containsExactly(tuple1, tuple2);
    }

    @Test
    void testListPartnersFilteredByNameLike() {
        PartnerDTO partnerDTO1 = template.postForObject("/api/partners", createPartnerCommand1, PartnerDTO.class);
        PartnerDTO partnerDTO2 = template.postForObject("/api/partners", createPartnerCommand2, PartnerDTO.class);

        String partOfCompanyName1 = partnerDTO1.getName().substring(1,4);
        String partOfCompanyName2 = partnerDTO2.getName().substring(1,4);

        List<PartnerDTO> partnerDTOS = template.exchange("/api/partners?nameLike=" + partOfCompanyName1,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<PartnerDTO>>(){})
                .getBody();

        assertThat(partnerDTOS)
                .hasSize(1)
                .extracting(PARAMETERS_FOR_TUPLE_NO_IBANS)
                .containsExactly(tuple1);

        List<PartnerDTO> partnerDTOS2 = template.exchange("/api/partners?nameLike=" + partOfCompanyName2,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<PartnerDTO>>(){})
                .getBody();

        assertThat(partnerDTOS2)
                .hasSize(1)
                .extracting(PARAMETERS_FOR_TUPLE_NO_IBANS)
                .containsExactly(tuple2);
    }

    @Test
    void testGetPartnerByGeneratedIdStartingCharP() {
        template.postForObject("/api/partners", createPartnerCommand1, PartnerDTO.class);
        PartnerDTO partnerDTO2 = template.postForObject("/api/partners", createPartnerCommand2, PartnerDTO.class);

        String id = partnerDTO2.getId();

        PartnerDTO partnerDTOResult = template.exchange("/api/partners/" + id,
                        HttpMethod.GET,
                        null,
                        PartnerDTO.class)
                .getBody();

        assertThat(List.of(partnerDTOResult))
                .extracting(PARAMETERS_FOR_TUPLE_NO_IBANS)
                .containsExactly(tuple2);

        assertTrue(id.startsWith("P-"));
    }

    @Test
    void testFindPartnerByIdAndAddIban() {
        template.postForObject("/api/partners", createPartnerCommand1, PartnerDTO.class);
        PartnerDTO partnerDTO2 = template.postForObject("/api/partners", createPartnerCommand2, PartnerDTO.class);
        String id = partnerDTO2.getId();

        template.put("/api/partners/" + id + "/ibans", new AddIbanCommand("FR123456-12345678-12345678"));
        template.put("/api/partners/" + id + "/ibans", new AddIbanCommand("FR654321-87654321-87654321"));

        PartnerDTO resultDto = template.exchange("/api/partners/" + id,
                        HttpMethod.GET,
                        null,
                        PartnerDTO.class)
                .getBody();

        assertEquals(Set.of("FR123456-12345678-12345678", "FR654321-87654321-87654321"), resultDto.getIbans());
    }

    @Test
    void testFindPartnerByIbanLike() {
        template.postForObject("/api/partners", createPartnerCommand1, PartnerDTO.class);
        PartnerDTO partnerDTO2 = template.postForObject("/api/partners", createPartnerCommand2, PartnerDTO.class);
        String partnerId2 = partnerDTO2.getId();

        template.put("/api/partners/" + partnerId2 + "/ibans", new AddIbanCommand("FR123456-12345678-12345678"));
        template.put("/api/partners/" + partnerId2 + "/ibans", new AddIbanCommand("FR654321-87654321-87654321"));

        String partOfIban = "FR654";

        List<PartnerDTO> partnerDTOS = template.exchange("/api/partners?iban=" + partOfIban,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<PartnerDTO>>(){})
                .getBody();

        assertThat(partnerDTOS)
                .hasSize(1)
                .extracting(PARAMETERS_FOR_TUPLE_NO_IBANS)
                .containsExactly(tuple2);

    }

    @Test
    void testFindPartnerByTaxNo() {
        template.postForObject("/api/partners", createPartnerCommand1, PartnerDTO.class);
        template.postForObject("/api/partners", createPartnerCommand2, PartnerDTO.class);

        String taxNo = "987654321";

        List<PartnerDTO> partnerDTOS = template.exchange("/api/partners?taxNo=" + taxNo,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<PartnerDTO>>(){})
                .getBody();

        assertThat(partnerDTOS)
                .hasSize(1)
                .extracting(PARAMETERS_FOR_TUPLE_NO_IBANS)
                .containsExactly(tuple2);
    }

    @Test
    void testShouldThrowPartnerNotFoundException() {
        template.postForObject("/api/partners", createPartnerCommand1, PartnerDTO.class);

        Problem result = template.getForObject("/api/partners/xxx", Problem.class);

        assertEquals(Status.NOT_FOUND, result.getStatus());
        assertEquals("Not found", result.getTitle());
        assertEquals("Partner not found: xxx", result.getDetail());

    }

    @Test
    void testDeletePartners() {
        PartnerDTO partnerDTO1 = template.postForObject("/api/partners", createPartnerCommand1, PartnerDTO.class);
        PartnerDTO partnerDTO2 = template.postForObject("/api/partners", createPartnerCommand2, PartnerDTO.class);

        List<PartnerDTO> partnerDTOS = template.exchange("/api/partners",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<PartnerDTO>>(){})
                .getBody();

        assertThat(partnerDTOS)
                .hasSize(2)
                .extracting(PARAMETERS_FOR_TUPLE_NO_IBANS)
                .containsExactly(tuple1, tuple2);

        template.delete("/api/partners/" + partnerDTO1.getId());
        template.delete("/api/partners/" + partnerDTO2.getId());

        partnerDTOS = template.exchange("/api/partners",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<PartnerDTO>>(){})
                .getBody();

        assertThat(partnerDTOS)
                .hasSize(0);
    }




}
