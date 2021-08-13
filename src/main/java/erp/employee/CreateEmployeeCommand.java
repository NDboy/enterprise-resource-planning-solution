package erp.employee;

import erp.Address;
import erp.IsCompleteAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

//@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeCommand {

    @NotBlank(message = "First name cannot be null or blank")
    @Length(max = 30, message = "First name maximum length is 30")
    @Schema(example = "Anthony")
    private String firstName;

    @NotBlank(message = "Last name cannot be null or blank")
    @Length(max = 30, message = "Last name maximum length is 30")
    @Schema(example = "Doerr")
    private String lastName;

    @NotNull
    private EmployeeStatus status;

    @IsCompleteAddress
    private Address address;

    @NotNull
    private LocalDate entryDate;

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
}
