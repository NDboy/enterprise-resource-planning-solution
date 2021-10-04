package erp.partner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import erp.general.Address;
import erp.general.IsCompleteAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePartnerCommand {

    @NotBlank(message = "Name cannot be null or blank")
    @Length(max = 50, message = "Name maximum length is 50")
    @Schema(example = "Tiger Ltd.")
    @JsonProperty
    private String name;

    @IsCompleteAddress
    @JsonProperty
    @Schema(example = "{\"country\": \"Hungary\", \"zipCode\": \"H-1025\", \"line\": \"Margit krt. 93.\"}")
    private Address address;

    @NotBlank(message = "Tax number cannot be null or blank")
    @Schema(example = "new11184972")
    @JsonProperty
    private String taxNo;

    @JsonProperty
    @Schema(example = "[\"new123654\", \"new84617\"]")
    private List<String> ibans;

}
