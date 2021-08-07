package erp.partner;

import erp.Address;
import erp.IsCompleteAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePartnerCommand {

    @NotBlank(message = "Name cannot be null or blank")
    @Length(max = 50, message = "Name maximum length is 50")
    @Schema(example = "Jaguar Ltd.")
    private String name;

    @IsCompleteAddress
    private Address address;


}
