package erp.apinvoice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceItem {

    @NotBlank(message = "Item name cannot be null or blank")
    @Length(max = 50, message = "Item name maximum length is 50")
    @Schema(example = "Mobile service")
    private String itemName;

    @NotBlank(message = "Net price cannot be blank")
    @Schema(example = "1000.0")
    private double netPrice;

    @NotBlank(message = "VAT rate cannot be blank")
    @Schema(example = "15")
    private int vatRate;

    private double vatAmount;

    private double grossPrice;

    private void countVatAmount() {
        vatAmount = netPrice * (vatRate / 100.0);
    }

    private void countGrossPrice() {
        grossPrice = netPrice * (1 + (vatRate / 100.0));
    }

    public InvoiceItem(String itemName, double netPrice, int vatRate) {
        this.itemName = itemName;
        this.netPrice = netPrice;
        this.vatRate = vatRate;
        countVatAmount();
        countGrossPrice();
    }

}
