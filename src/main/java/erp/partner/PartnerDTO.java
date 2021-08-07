package erp.partner;

import erp.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerDTO {

    private String id;

    private String name;

    private Address address;

    private String taxNo;

    private Set<String> ibans;

}
