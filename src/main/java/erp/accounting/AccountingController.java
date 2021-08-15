package erp.accounting;

import erp.apinvoice.InvoiceStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @Operation(summary = "list all accountings or filter by A/P invoice id, (filtering syntax = /api/accountings?invoiceid=invoice id string)",
            description = "list all accountings or filter by A/P invoice id")
    public List<AccountingDTO> listAccountingOrFilterByInvoiceId(@RequestParam Optional<String> invoiceid) {
        return accountingService.listAccountingOrFilterByInvoiceId(invoiceid);
    }

    @GetMapping("/apinvoicestatus")
    @Operation(summary = "Filter by A/P invoice status, (filtering syntax = /api/accountings/apinvoicestatus?invoicestatus=OPEN)",
            description = "Filter by A/P invoice status")
    public List<AccountingDTO> listByInvoiceStatus(@RequestParam InvoiceStatus invoiceStatus) {
        return accountingService.listByInvoiceStatus(invoiceStatus);
    }
}
