package erp.apinvoice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateAPInvoiceCommand {

    @NotBlank(message = "Invoice number cannot be null or blank")
    @Length(max = 20, message = "Invoice number maximum length is 20")
    @Schema(example = "23541687298")
    private String invNum;

    @IsValidPaymentModeAndDates
    private PaymentModeAndDates paymentModeAndDates;

    @NotNull(message = "Invoice can not be created without invoice status")
    @Schema(example = "OPEN")
    private InvoiceStatus invoiceStatus;

    @IsValidInvoiceItems
    @NotNull(message = "Invoice can not be created without items")
    private List<InvoiceItem> invoiceItems;

}
