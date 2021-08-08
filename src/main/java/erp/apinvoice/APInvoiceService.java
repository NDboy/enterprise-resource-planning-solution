package erp.apinvoice;


import erp.acounting.Accounting;
import erp.acounting.AccountingService;
import erp.employee.Employee;
import erp.employee.EmployeeNotFoundException;
import erp.employee.EmployeeRepository;
import erp.employee.EmployeeService;
import erp.partner.Partner;
import erp.partner.PartnerNotFoundException;
import erp.partner.PartnerRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class APInvoiceService {

    private ModelMapper modelMapper;

    private APInvoiceRepository apInvoiceRepository;

    private PartnerRepository partnerRepository;

    private AccountingService accountingService;

    private EmployeeRepository employeeRepository;

    public APInvoiceDTO createAPInvoice(CreateAPInvoiceCommand command) {
        List<InvoiceItem> invoiceItems = command.getInvoiceItems()
                .stream()
                .map(ii -> new InvoiceItem(ii.getItemName(), ii.getNetPrice(), ii.getVatRate()))
                .collect(Collectors.toList());
        APInvoice apInvoice = new APInvoice(command.getInvNum(), command.getPaymentModeAndDates(), command.getInvoiceStatus(), invoiceItems);
        apInvoiceRepository.save(apInvoice);
        return modelMapper.map(apInvoice, APInvoiceDTO.class);
    }


    public List<APInvoiceDTO> listAPInvoicesOrFilteredByPartnerId(Optional<String> partnerid) {
        List<APInvoice> apInvoices;
        if (partnerid.isEmpty()) {
            apInvoices = apInvoiceRepository.findAll();
        } else {
            apInvoices = apInvoiceRepository.findAllByPartnerId(partnerid.get());
        }
        Type targetListType = new TypeToken<List<APInvoiceDTO>>() {}.getType();
        return modelMapper.map(apInvoices, targetListType);
    }

    public APInvoiceDTO findAPInvoiceById(String id) {
        APInvoice apInvoice = apInvoiceRepository.findById(id)
                .orElseThrow(() -> new APInvoiceNotFoundException(id));
        return modelMapper.map(apInvoice, APInvoiceDTO.class);
    }

    @Transactional
    public APInvoiceDTO addNewPartner(String id, AddNewPartnerCommand command) {
        APInvoice apInvoice = apInvoiceRepository.findById(id)
                .orElseThrow(() -> new APInvoiceNotFoundException(id));
        String partnerId = command.getPartnerId();
        Partner partner = partnerRepository.findById(partnerId).orElseThrow(() -> new PartnerNotFoundException(partnerId));
        partnerRepository.save(partner);

        return modelMapper.map(apInvoice, APInvoiceDTO.class);
    }

    @Transactional
    public APInvoiceDTO addNewEmployee(String id, AddNewEmployeeCommand command) {
        APInvoice apInvoice = apInvoiceRepository.findById(id)
                .orElseThrow(() -> new APInvoiceNotFoundException(id));
        String employeeId = command.getEmployeeId();
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        employeeRepository.save(employee);

        Accounting accounting = new Accounting(LocalDate.now(), employee, apInvoice.getInvoiceStatus(), apInvoice);
        accountingService.createAccounting(accounting);

        return modelMapper.map(apInvoice, APInvoiceDTO.class);
    }

    public List<APInvoiceDTO> listAPInvoicesOrFilterByStatus(InvoiceStatus invoiceStatus) {
        List<APInvoice> apInvoices = apInvoiceRepository.findAllByInvoiceStatus(invoiceStatus);
        Type targetListType = new TypeToken<List<APInvoice>>() {}.getType();
        return modelMapper.map(apInvoices, targetListType);
    }

    @Transactional
    public APInvoiceDTO changeInvoiceStatus(String id, ChangeInvoiceStatusCommand command) {
        APInvoice apInvoice = apInvoiceRepository.findById(id)
                .orElseThrow(() -> new APInvoiceNotFoundException(id));
        apInvoice.setInvoiceStatus(command.getInvoiceStatus());
        String employeeId = command.getEmployeeId();
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        accountingService.createAccounting(new Accounting(LocalDate.now(), employee, command.getInvoiceStatus(), apInvoice));

        return modelMapper.map(apInvoice, APInvoiceDTO.class);
    }
}

