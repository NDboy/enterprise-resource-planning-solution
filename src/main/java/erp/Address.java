package erp;


import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Embeddable
public class Address {

    @Schema(example = "Hungary")
    public String country;


    @Schema(example = "H-1026")
    public String zipCode;


    @Schema(example = "Pasareti ut 101.")
    public String line;

    public Address() {
    }

    public Address(String country, String zipCode, String line) {
        this.country = country;
        this.zipCode = zipCode;
        this.line = line;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return country + " " + zipCode + " " + line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(country, address.country) && Objects.equals(zipCode, address.zipCode) && Objects.equals(line, address.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, zipCode, line);
    }
}
