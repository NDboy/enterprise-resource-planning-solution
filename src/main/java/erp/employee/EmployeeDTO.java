package erp.employee;

import erp.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

    private String id;

    private String firstName;

    private String lastName;

    private EmployeeStatus status;

    private Address address;

    private LocalDate entryDate;

}
