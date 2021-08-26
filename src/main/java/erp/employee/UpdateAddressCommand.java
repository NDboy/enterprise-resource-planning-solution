package erp.employee;

import erp.general.Address;
import erp.general.IsCompleteAddress;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

//@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAddressCommand {

    @IsCompleteAddress
    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
