package erp.apinvoice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/apinvoices")
@AllArgsConstructor
@Tag(name = "Operations on A/P invoices")
public class APInvoiceController {

    APInvoiceService apInvoiceService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create an A/P invoice with existing Partner and Employee id", description = "Create an A/P invoice with existing Partner and Employee id")
    public APInvoiceDTO createAPInvoiceWithPartnerAndEmployeeId(@Valid @RequestBody CreateAPInvoiceWithPartnerAndEmployeeIdCommand command) {
        return apInvoiceService.createAPInvoiceWithPartnerAndEmployeeId(command);
    }

    @GetMapping
    @Operation(summary = "Filter invoices by different parameters, \n" +
            "(examples: invNum, paymentMode, dueDate is before , partnerId, employeeId, invoiceStatus, accountingDate is after; filtering syntax = /api/apinvoices?partnerid=companyId)",
            description = "Filter invoices by different parameters, \n" +
                    "\"(examples: invNum, paymentMode, dueDate is before, partnerId, employeeId, invoiceStatus, accountingDate is after; filtering syntax = /api/apinvoices?partnerid=companyId)")
    public List<APInvoiceDTO> filterAPInvoicesByDifferentParameters(@RequestParam Map<String, String> params) {
        return apInvoiceService.filterInvoicesByDifferentParams(params);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find A/P invoice by id", description = "Find A/P invoice by id")
    public APInvoiceDTO findAPInvoiceById(@PathVariable("id") String id) {
        return apInvoiceService.findAPInvoiceById(id);
    }

    @PutMapping("/{id}/partner")
    @Operation(summary = "Find A/P invoice by id and add new partner, putting an existing partner id, no need to fill other data",
            description = "Find A/P invoice by id and add new partner, putting an existing partner id, no need to fill other data")
    public APInvoiceDTO addPartner(@PathVariable("id") String id, @RequestBody AddNewPartnerCommand command) {
        return apInvoiceService.addNewPartner(id, command);
    }

    @PutMapping("/{id}/invoicestatus")
    @Operation(summary = "Find A/P invoice by id and change invoice status",
            description = "Find A/P invoice by id and change invoice status")
    public APInvoiceDTO changeInvoiceStatus(@PathVariable("id") String id, @RequestBody ChangeInvoiceStatusCommand command) {
        return apInvoiceService.changeInvoiceStatus(id, command);
    }

    @PutMapping("/{id}/employee")
    @Operation(summary = "Find A/P invoice by id and add new employee, putting an existing employee id, no need to fill other data",
            description = "Find A/P invoice by id and add new employee, putting an existing employee id, no need to fill other data")
    public APInvoiceDTO addEmployee(@PathVariable("id") String id, @RequestBody AddNewEmployeeCommand command) {
        return apInvoiceService.addNewEmployee(id, command);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete A/P invoice by id", description = "Delete A/P invoice by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAPInvoiceById(@Parameter(example = "E21-1") @PathVariable("id") String id) {
        apInvoiceService.deleteAPInvoiceById(id);
    }




}
