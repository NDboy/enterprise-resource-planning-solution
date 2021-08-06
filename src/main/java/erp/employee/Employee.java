package erp.employee;

import erp.Address;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@Table(name = "employees")
public class Employee {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    @Enumerated(value = EnumType.STRING)
    private EmployeeStatus status;

    @Embedded
    private Address address;

    private LocalDate entryDate;

    public Employee() {
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status=" + status +
                ", address=" + address +
                ", entryDate=" + entryDate +
                '}';
    }
}
