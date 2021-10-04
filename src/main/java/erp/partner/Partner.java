package erp.partner;

import erp.general.Address;
import erp.apinvoice.APInvoice;
import erp.partner.dto.UpdatePartnerCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "partners")
@Data
public class Partner {

    @Id
    @GeneratedValue(generator = "partner-generator")
    @GenericGenerator(name = "partner-generator",
            parameters = @Parameter(name = "prefix", value = "P"),
            strategy = "erp.general.MyIdGenerator")
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
    private List<String> ibans = new ArrayList<>();

    @OneToMany(mappedBy = "partner")
    private List<APInvoice> apInvoices;

    public Partner(String name, Address address, String taxNo) {
        this.name = name;
        this.address = address;
        this.taxNo = taxNo;
    }

    public Partner(String name, Address address, String taxNo, List<String> ibans) {
        this.name = name;
        this.address = address;
        this.taxNo = taxNo;
        this.ibans = ibans;
    }

    public void addIban(String iban) {
        if (ibans == null) {
            ibans = new ArrayList<>();
        }
        ibans.add(iban);
    }

    public void update(UpdatePartnerCommand command) {
        String newName = command.getName();
        Address newAddress = command.getAddress();
        String newTaxNo = command.getTaxNo();
        List<String> newIbans = command.getIbans();
        if (!empty(newName)) {
            setName(newName);
        }
        if (newAddress != null) {
            setAddress(newAddress);
        }
        if (!empty(newTaxNo)) {
            setTaxNo(newTaxNo);
        }
        if (newIbans != null && !newIbans.isEmpty()) {
            setIbans(newIbans);
        }
    }

    private boolean empty(String s){
        return s == null || s.isBlank();
    }
}
