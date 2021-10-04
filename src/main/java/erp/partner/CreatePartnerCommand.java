package erp.partner;

import erp.general.Address;
import erp.general.IsCompleteAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatePartnerCommand {

    @NotBlank(message = "Name cannot be null or blank")
    @Length(max = 50, message = "Name maximum length is 50")
    @Schema(example = "Jaguar Ltd.")
    private String name;

    @IsCompleteAddress
    @Schema(example = "{\"country\": \"Hungary\", \"zipCode\": \"H-1026\", \"line\": \"Pasaréti út 101.\"}")
    private Address address;

    @NotBlank(message = "Tax number cannot be null or blank")
    @Schema(example = "9235684972")
    private String taxNo;

    @Schema(example = "[\"123654\", \"84617\"]")
    private List<String> ibans;

}
