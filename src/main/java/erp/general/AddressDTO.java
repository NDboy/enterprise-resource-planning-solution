package erp.general;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressDTO {

    public String country;

    public String zipCode;

    public String line;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressDTO that = (AddressDTO) o;
        return Objects.equals(country, that.country) && Objects.equals(zipCode, that.zipCode) && Objects.equals(line, that.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, zipCode, line);
    }
}
