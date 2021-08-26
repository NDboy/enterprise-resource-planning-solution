package erp.employee;

import erp.general.Address;
import erp.general.IsCompleteAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAddressCommand {

    @IsCompleteAddress
    private Address address;

}
