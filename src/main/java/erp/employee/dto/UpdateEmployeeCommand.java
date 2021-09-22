package erp.employee.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import erp.employee.EmployeeStatus;
import erp.general.Address;
import erp.general.IsCompleteAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeCommand {

    @NotBlank(message = "Name cannot be null or blank")
    @Length(max = 30, message = "Name maximum length is 30")
    @JsonProperty
    @Schema(example = "Anthony")
    private String firstName;

    @NotBlank(message = "Name cannot be null or blank")
    @Length(max = 30, message = "Name maximum length is 30")
    @JsonProperty
    @Schema(example = "Doerr")
    private String lastName;

    @JsonProperty
    @Schema(example = "QUIT")
    private EmployeeStatus status;

    @IsCompleteAddress
    @JsonProperty
    private Address address;

    @NotNull
    @JsonProperty
    @Schema(example = "2021-09-13")
    private LocalDate entryDate;


}
