package erp.apinvoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class VatItem {

    private double vatRate;
    private double vatAmount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VatItem vatItem = (VatItem) o;
        return Double.compare(vatItem.vatRate, vatRate) == 0 && Double.compare(vatItem.vatAmount, vatAmount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vatRate, vatAmount);
    }
}
