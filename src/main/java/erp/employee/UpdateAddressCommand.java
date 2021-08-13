package erp.employee;

import erp.Address;
import erp.IsCompleteAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

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
