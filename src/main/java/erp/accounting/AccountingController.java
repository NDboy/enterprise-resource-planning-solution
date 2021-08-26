package erp.accounting;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accountings")
@AllArgsConstructor
@Tag(name = "requests on accountings")
public class AccountingController {

    AccountingService accountingService;

    @GetMapping("/{id}")
    @Operation(summary = "find accounting by id", description = "find accounting by id")
    public AccountingDTO findAccountingById(@PathVariable("id") String id) {
        return accountingService.findAccountingById(id);
    }

    @GetMapping
    @Operation(summary = "list all accountings or filter by different parameters: \n" +
            "accountingDate, employeeId, employeeLastName, invoiceStatus, apInvoiceId\n (filtering syntax = /api/accountings?invoiceid=invoiceid string)",
            description = "list all accountings or filter by different parameters: \n" +
                    "accountingDate, employeeId, employeeLastName, invoiceStatus, apInvoiceId")
    public List<AccountingDTO> listAccountingOrFilterByDifferentParameters(@RequestParam Map<String, String> params) {
        return accountingService.listAccountingOrFilterByDifferentParameters(params);
    }


}
