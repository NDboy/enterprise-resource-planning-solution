package erp.partner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

//@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddIbanCommand {

    @NotBlank(message = "Iban cannot be null or blank")
    @Length(max = 30, message = "Iban maximum length is 30")
    @Schema(example = "HU123456-12345678")
    private String iban;

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }
}
