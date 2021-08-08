package erp.apinvoice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangeInvoiceStatusCommand {

    @NotNull(message = "OPEN, PAYED or CANCELED")
    @Schema(example = "CANCELED")
    private InvoiceStatus invoiceStatus;

}
