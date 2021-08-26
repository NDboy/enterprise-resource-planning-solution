package erp.apinvoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceItemDTO {

    private String itemName;

    private double netPrice;

    private int vatRate;

    private double vatAmount;

    private double grossPrice;

}
