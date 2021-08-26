package erp.apinvoice;


import erp.accounting.Accounting;
import erp.accounting.AccountingService;
import erp.employee.Employee;
import erp.employee.EmployeeNotFoundException;
import erp.employee.EmployeeRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class APInvoiceService {

    private ModelMapper modelMapper;

    private APInvoiceRepository apInvoiceRepository;

    private PartnerRepository partnerRepository;

    private AccountingService accountingService;

    private EmployeeRepository employeeRepository;


    public APInvoiceDTO createAPInvoiceWithPartnerAndEmployeeId(CreateAPInvoiceWithPartnerAndEmployeeIdCommand command) {
        List<InvoiceItem> invoiceItems = command.getInvoiceItems()
                .stream()
                .map(ii -> new InvoiceItem(ii.getItemName(), ii.getNetPrice(), ii.getVatRate()))
                .collect(Collectors.toList());
        APInvoice apInvoice = new APInvoice(command.getInvNum(), command.getPaymentModeAndDates(), command.getInvoiceStatus(), invoiceItems);
        Partner partner = null;
        Employee employee = null;
        if (command.getPartnerId() != null && !command.getPartnerId().isBlank()) {
            partner = partnerRepository.findById(command.getPartnerId()).orElseThrow(() -> new PartnerNotFoundException(command.getPartnerId()));
            apInvoice.setPartner(partner);
        }
        if (command.getEmployeeId() != null && !command.getEmployeeId().isBlank()) {
            employee = employeeRepository.findById(command.getEmployeeId()).orElseThrow(() -> new EmployeeNotFoundException(command.getEmployeeId()));
            apInvoice.setEmployee(employee);
        }
        apInvoiceRepository.save(apInvoice);

        if (partner != null && employee != null) {
            Accounting accounting = new Accounting(LocalDate.now(), employee, apInvoice.getInvoiceStatus(), apInvoice);
            accountingService.createAccounting(accounting);
        }

        return modelMapper.map(apInvoice, APInvoiceDTO.class);

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

        apInvoice.setPartner(partner);
        return modelMapper.map(apInvoice, APInvoiceDTO.class);
    }

    @Transactional
    public APInvoiceDTO addNewEmployee(String id, AddNewEmployeeCommand command) {
        APInvoice apInvoice = apInvoiceRepository.findById(id)
                .orElseThrow(() -> new APInvoiceNotFoundException(id));
        String employeeId = command.getEmployeeId();
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        apInvoice.setEmployee(employee);
        Accounting accounting = new Accounting(LocalDate.now(), employee, apInvoice.getInvoiceStatus(), apInvoice);
        accountingService.createAccounting(accounting);
        return modelMapper.map(apInvoice, APInvoiceDTO.class);
    }

    @Transactional
    public APInvoiceDTO changeInvoiceStatus(String id, ChangeInvoiceStatusCommand command) {
        APInvoice apInvoice = apInvoiceRepository.findById(id)
                .orElseThrow(() -> new APInvoiceNotFoundException(id));
        apInvoice.setInvoiceStatus(command.getInvoiceStatus());
        String employeeId = command.getEmployeeId();
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        apInvoice.setEmployee(employee);
        Accounting accounting = new Accounting(LocalDate.now(), employee, command.getInvoiceStatus(), apInvoice);
        accountingService.createAccounting(accounting);

        return modelMapper.map(apInvoice, APInvoiceDTO.class);
    }


    public List<APInvoiceDTO> filterInvoicesByDifferentParams(Map<String, String> params) {
        List<APInvoice> apInvoices = new ArrayList<>();
        Type targetListType = new TypeToken<List<APInvoiceDTO>>() {}.getType();
        if (params == null || params.isEmpty()) {
            apInvoices = apInvoiceRepository.findAll();
            return modelMapper.map(apInvoices, targetListType);
        }
        if (params.containsKey("invNum")) {
            apInvoices = apInvoiceRepository.findByInvNumEqualsIgnoreCase(params.get("invNum"));
        } else if (params.containsKey("paymentMode")){
            apInvoices = apInvoiceRepository.findByPaymentMode(PaymentMode.valueOf(params.get("paymentMode")));
        } else if (params.containsKey("dueDate")){
            apInvoices = apInvoiceRepository.findByDueDateIsBefore(LocalDate.parse(params.get("dueDate")));
        } else if (params.containsKey("partnerId")){
            apInvoices = apInvoiceRepository.findByPartnerId(params.get("partnerId"));
        } else if (params.containsKey("employeeId")){
            apInvoices = apInvoiceRepository.findByEmployeeId(params.get("employeeId"));
        } else if (params.containsKey("invoiceStatus")){
            apInvoices = apInvoiceRepository.findAllByInvoiceStatus(InvoiceStatus.valueOf(params.get("invoiceStatus")));
        } else if (params.containsKey("accountingDate")){
            apInvoices = apInvoiceRepository.findByAccountingDateIsAfter(LocalDate.parse(params.get("accountingDate")));
        }
        return modelMapper.map(apInvoices, targetListType);
    }

    public void deleteAPInvoiceById(String id) {
        apInvoiceRepository.deleteById(id);
    }
}

