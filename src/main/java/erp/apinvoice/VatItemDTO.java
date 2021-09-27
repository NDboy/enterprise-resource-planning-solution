package erp.apinvoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VatItemDTO {

    private double vatRate;
    private double vatAmount;

}
