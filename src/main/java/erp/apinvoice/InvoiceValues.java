package erp.apinvoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import java.util.List;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceValues {

    private double netValue;

    private double vatTotal;

    private double grossValue;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<VatItem> vatAmounts;



}
