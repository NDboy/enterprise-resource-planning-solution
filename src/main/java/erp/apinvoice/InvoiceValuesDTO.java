package erp.apinvoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.List;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceValuesDTO {

    private double netValue;

    private double vatTotal;

    private double grossValue;

    private List<VatItem> vatAmounts;

}
