package erp.employee;

import erp.Address;
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

import static erp.employee.EmployeeStatus.ACTIVE;
import static erp.employee.EmployeeStatus.PASSIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from employees"})
public class EmployeeControllerRestTemplateIT {

    @Autowired
    TestRestTemplate template;

    private CreateEmployeeCommand createEmployeeCommand1;
    private CreateEmployeeCommand createEmployeeCommand2;
    private Tuple tuple1;
    private Tuple tuple2;


    private final String[] PARAMETERS_FOR_TUPLE = new String[] {"id", "firstName", "lastName", "status", "address", "entryDate"};
    private Address address;

    @BeforeEach
    void init() {
        address = new Address("Hungary", "H-1029", "Pasareti ut 101.");

        createEmployeeCommand1 = new CreateEmployeeCommand("Anthony", "Doerr", PASSIVE, address, LocalDate.of(2021,8,5));
        tuple1 = tuple("ado", "Anthony", "Doerr", PASSIVE, address, LocalDate.of(2021,8,5));

        createEmployeeCommand2 = new CreateEmployeeCommand("Jo", "Nesbo", ACTIVE, address, LocalDate.now());
        tuple2 = tuple("jne", "Jo", "Nesbo", ACTIVE, address, LocalDate.now());

    }

    @Test
    void testCreateAndListEmployees() {
        template.postForObject("/api/employees", createEmployeeCommand1, EmployeeDTO.class);
        template.postForObject("/api/employees", createEmployeeCommand2, EmployeeDTO.class);

        List<EmployeeDTO> employeeDTOS = template.exchange("/api/employees",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<EmployeeDTO>>(){})
                .getBody();

        assertThat(employeeDTOS)
                .hasSize(2)
                .extracting(PARAMETERS_FOR_TUPLE)
                .containsExactly(tuple1, tuple2);
    }

    @Test
    void testListEmployeesFilteredByStatus() {
        template.postForObject("/api/employees", createEmployeeCommand1, EmployeeDTO.class);
        template.postForObject("/api/employees", createEmployeeCommand2, EmployeeDTO.class);

        List<EmployeeDTO> employeeDTOS = template.exchange("/api/employees?status=ACTIVE",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<EmployeeDTO>>(){})
                .getBody();

        assertThat(employeeDTOS)
                .hasSize(1)
                .extracting(PARAMETERS_FOR_TUPLE)
                .containsExactly(tuple2);
    }

    @Test
    void testGetEmployeeById() {
        EmployeeDTO employeeDTO1 = template.postForObject("/api/employees", createEmployeeCommand1, EmployeeDTO.class);
        template.postForObject("/api/employees", createEmployeeCommand2, EmployeeDTO.class);
        String id1 = employeeDTO1.getId();

        EmployeeDTO result = template.exchange("/api/employees/" + id1,
                        HttpMethod.GET,
                        null,
                        EmployeeDTO.class)
                .getBody();

        assertThat(List.of(result))
                .extracting(PARAMETERS_FOR_TUPLE)
                .containsExactly(tuple1);
    }

    @Test
    void testFindEmployeeAndChangeStatus() {
        EmployeeDTO employeeDTO1 = template.postForObject("/api/employees", createEmployeeCommand1, EmployeeDTO.class);
        String id1 = employeeDTO1.getId();
        EmployeeStatus statusOriginal = employeeDTO1.getStatus();

        template.put("/api/employees/" + id1 + "/status",
                        new UpdateStatusCommand(ACTIVE));

        EmployeeDTO result = template.exchange("/api/employees/" + id1,
                        HttpMethod.GET,
                        null,
                        EmployeeDTO.class)
                .getBody();

        assertEquals(PASSIVE, statusOriginal);
        assertEquals(ACTIVE, result.getStatus());
    }

    @Test
    void testFindEmployeeAndChangeAddress() {
        EmployeeDTO employeeDTO1 = template.postForObject("/api/employees", createEmployeeCommand1, EmployeeDTO.class);
        String id1 = employeeDTO1.getId();
        Address addressOriginal = employeeDTO1.getAddress();

        Address addressChanged = new Address("Hungary", "H-1025", "Hazman u. 5.");
        template.put("/api/employees/" + id1 + "/address",
                new UpdateAddressCommand(addressChanged));

        EmployeeDTO result = template.exchange("/api/employees/" + id1,
                        HttpMethod.GET,
                        null,
                        EmployeeDTO.class)
                .getBody();

        assertEquals(address, addressOriginal);
        assertEquals(addressChanged, result.getAddress());
    }

    @Test
    void testShouldThrowEmployeeNotFoundException() {
        template.postForObject("/api/employees", createEmployeeCommand1, EmployeeDTO.class);

        Problem result = template.getForObject("/api/employees/xxx", Problem.class);

        assertEquals(Status.NOT_FOUND, result.getStatus());
        assertEquals("Not found", result.getTitle());
        assertEquals("Employee with id: xxx not found", result.getDetail());

    }

}
