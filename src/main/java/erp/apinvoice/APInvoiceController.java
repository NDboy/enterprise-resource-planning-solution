package erp.apinvoice;

import erp.partner.PartnerDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/apinvoices")
@AllArgsConstructor
@Tag(name = "operations on A/P invoices")
public class APInvoiceController {

    APInvoiceService apInvoiceService;

    @PostMapping
    @Operation(summary = "create an A/P invoice", description = "create an A/P invoice")
    public APInvoiceDTO createAPInvoice(@Valid @RequestBody CreateAPInvoiceCommand command) {
        return apInvoiceService.createAPInvoice(command);
    }

    @GetMapping
    @Operation(summary = "list all invoices or filter by partner id, (filtering syntax = /api/apinvoices?partnerid=companyId)",
            description = "list all invoices or filter by partner id")
    public List<APInvoiceDTO> listAPInvoicesOrFilteredByPartnerId(@RequestParam Optional<String> partnerid) {
        return apInvoiceService.listAPInvoicesOrFilteredByPartnerId(partnerid);
    }

    @GetMapping("/{id}")
    @Operation(summary = "find A/P invoice by id", description = "find A/P invoice by id")
    public APInvoiceDTO findAPInvoiceById(@PathVariable("id") String id) {
        return apInvoiceService.findAPInvoiceById(id);
    }

    @GetMapping("/invoicestatus")
    @Operation(summary = "list all invoices or filter by status, (filtering syntax = /api/apinvoices/invoicestatus?invoiceStatus=OPEN)",
            description = "list all invoices or filter by status")
    public List<APInvoiceDTO> listAPInvoicesOrFilteredByStatus(@RequestParam InvoiceStatus invoiceStatus) {
        return apInvoiceService.listAPInvoicesOrFilterByStatus(invoiceStatus);
    }

    @PutMapping("/{id}/partner")
    @Operation(summary = "find A/P invoice by id and add new partner, putting an existing partner id, no need to fill other data",
            description = "find A/P invoice by id and add new partner, putting an existing partner id, no need to fill other data")
    public APInvoiceDTO addPartner(@PathVariable("id") String id, @RequestBody AddNewPartnerCommand command) {
        return apInvoiceService.addNewPartner(id, command);
    }

    @PutMapping("/{id}/invoicestatus")
    @Operation(summary = "find A/P invoice by id and change invoice status",
            description = "find A/P invoice by id and change invoice status")
    public APInvoiceDTO changeInvoiceStatus(@PathVariable("id") String id, @RequestBody ChangeInvoiceStatusCommand command) {
        return apInvoiceService.changeInvoiceStatus(id, command);
    }








}
