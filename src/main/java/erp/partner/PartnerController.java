package erp.partner;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/partners")
@AllArgsConstructor
@Tag(name = "operations on partners")
public class PartnerController {

    PartnerService partnerService;

    @GetMapping
    @Operation(summary = "list all partners or filter by name, (filtering syntax = /api/partners?name=companyName)",
            description = "list all partners or filter by name")
    public List<PartnerDTO> listPartnersByNameLike(@RequestParam Optional<String> name) {
        return partnerService.listPartnersByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "create a partner", description = "create a partner")
    public PartnerDTO createPartner(@Valid @RequestBody CreatePartnerCommand command) {
        return partnerService.createPartner(command);
    }

    @GetMapping("/{id}")
    @Operation(summary = "find partner by id", description = "find partner by id")
    public PartnerDTO findPartnerById(@PathVariable("id") String id) {
        return partnerService.findPartnerById(id);
    }

    @PutMapping("/{id}/ibans")
    @Operation(summary = "find partner by id and add new iban",
            description = "find partner by id and add new iban")
    public PartnerDTO addIban(@PathVariable("id") String id, @Valid @RequestBody AddIbanCommand command) {
        return partnerService.addIban(id, command);
    }

    @GetMapping("/ibans")
    @Operation(summary = "find partner by iban",
            description = "find partner by iban")
    public PartnerDTO findPartnerByIban(@RequestParam String iban) {
        return partnerService.findPartnerByIban(iban);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Partner by id without APInvoice", description = "Delete Partner by id without APInvoice")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePartnerById(@Parameter(example = "P-1") @PathVariable("id") String id) {
        partnerService.deletePartnerById(id);
    }


}
