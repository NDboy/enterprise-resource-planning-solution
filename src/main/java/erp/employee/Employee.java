package erp.employee;

import erp.general.Address;
import erp.accounting.Accounting;
import erp.apinvoice.APInvoice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        generateIdByName(firstName, lastName);
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.address = address;
        this.entryDate = entryDate;
    }

    public Employee(String firstName, String lastName) {
        generateIdByName(firstName, lastName);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private void generateIdByName(String firstName, String lastName) {
        id = firstName.toLowerCase().substring(0,1) + lastName.toLowerCase().substring(0,2);
    }

}
