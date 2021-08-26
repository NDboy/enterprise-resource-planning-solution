package erp.apinvoice;

import erp.employee.EmployeeDTO;
import erp.partner.PartnerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class APInvoiceDTO {

    private String id;

    private String invNum;

    private PaymentModeAndDatesDTO paymentModeAndDates;

    private PartnerDTO partner;

    private InvoiceStatus invoiceStatus;

    private List<InvoiceItemDTO> invoiceItems;

    private EmployeeDTO employee;

}
