package erp.partner;

import erp.Address;
import erp.apinvoice.APInvoice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "partners")
public class Partner {

    @Id
    @GeneratedValue(generator = "partner-generator")
    @GenericGenerator(name = "partner-generator",
            parameters = @Parameter(name = "prefix", value = "P"),
            strategy = "erp.MyIdGenerator")
    @Schema(example = "P-2")
    private String id;

    @Schema(example = "Jaguar-PartnerSchema")
    private String name;

    @Embedded
    private Address address;

    @Schema(example = "26841937-PartnerSchema")
    private String taxNo;

    @Schema(example = "[\"123654\", \"84617\"]")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> ibans = new HashSet<>();

    @OneToMany(mappedBy = "partner")
    private List<APInvoice> apInvoices;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public Set<String> getIbans() {
        return ibans;
    }

    public void setIbans(Set<String> ibans) {
        this.ibans = ibans;
    }

    public List<APInvoice> getApInvoices() {
        return apInvoices;
    }

    public void setApInvoices(List<APInvoice> apInvoices) {
        this.apInvoices = apInvoices;
    }
}
