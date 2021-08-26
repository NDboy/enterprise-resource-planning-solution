package erp.employee;

import erp.general.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeDTO {

    private String id;

    private String firstName;

    private String lastName;

    private EmployeeStatus status;

    private AddressDTO address;

    private LocalDate entryDate;

}
