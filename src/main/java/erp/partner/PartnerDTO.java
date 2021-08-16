package erp.partner;

import erp.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
public class PartnerDTO {

    private String id;

    private String name;

    private AddressDTO address;

    private String taxNo;

    private Set<String> ibans;

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

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
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


}
