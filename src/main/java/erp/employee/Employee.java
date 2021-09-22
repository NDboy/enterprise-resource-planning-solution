package erp.employee;

import erp.employee.dto.UpdateEmployeeCommand;
import erp.general.Address;
import erp.accounting.Accounting;
import erp.apinvoice.APInvoice;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Table(name = "employees")
@Data
public class Employee {

    @Id
    @GeneratedValue(generator = "employee-generator")
    @GenericGenerator(name = "employee-generator", strategy = "erp.employee.EmployeeIdGenerator")
    private String id;

    @Schema(example = "Anthony")
    private String firstName;

    @Schema(example = "Doerr")
    private String lastName;

    @Enumerated(value = EnumType.STRING)
    private EmployeeStatus status;

    @Embedded
    private Address address;

    private LocalDate entryDate;

    @OneToMany(mappedBy = "employee")
    private List<APInvoice> apInvoices;

    @OneToMany(mappedBy = "employee")
    private List<Accounting> accountings;

    public Employee(String firstName, String lastName, EmployeeStatus status, Address address, LocalDate entryDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.address = address;
        this.entryDate = entryDate;
    }

    public Employee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void update(UpdateEmployeeCommand command) {
        String newFirstName = command.getFirstName();
        String newLastName = command.getLastName();
        EmployeeStatus newStatus = command.getStatus();
        Address newAddress = command.getAddress();
        LocalDate newEntryDate = command.getEntryDate();
        if (!empty(newFirstName)) {
            setFirstName(newFirstName);
        }
        if (!empty(newLastName)) {
            setLastName(newLastName);
        }
        if (newStatus != null) {
            setStatus(newStatus);
        }
        if (newAddress != null) {
            setAddress(newAddress);
        }
        if (newEntryDate != null) {
            setEntryDate(newEntryDate);
        }
    }

    private boolean empty(String s){
        return s == null || s.isBlank();
    }

}
