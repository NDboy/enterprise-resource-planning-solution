package erp.partner;

import erp.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "partners")
public class Partner {

    @Id
    @GeneratedValue(generator = "partner-generator")
    @GenericGenerator(name = "partner-generator",
            parameters = @Parameter(name = "prefix", value = "P"),
            strategy = "erp.MyIdGenerator")
    private String id;

    private String name;

    private Address address;

    private String taxNo;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> ibans;

    public Partner(String name, Address address, String taxNo) {
        this.name = name;
        this.address = address;
        this.taxNo = taxNo;
    }

    public void addIban(String iban) {
        if (ibans == null) {
            ibans = new HashSet<>();
        }
        ibans.add(iban);
    }
}
