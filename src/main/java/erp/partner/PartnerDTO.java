package erp.partner;

import erp.general.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartnerDTO {

    private String id;

    private String name;

    private AddressDTO address;

    private String taxNo;

    private List<String> ibans;

}
