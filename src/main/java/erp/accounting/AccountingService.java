package erp.accounting;

import erp.apinvoice.InvoiceStatus;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@AllArgsConstructor
public class AccountingService {

    private AccountingRepository accountingRepository;

    private ModelMapper modelMapper;

    public AccountingDTO createAccounting(Accounting accounting) {
        accountingRepository.save(accounting);
        return modelMapper.map(accounting, AccountingDTO.class);
    }

    public AccountingDTO findAccountingById(String id) {
        Accounting accounting = accountingRepository.findById(id).
                orElseThrow(() -> new AccountingNotFoundException(id));
        return modelMapper.map(accounting, AccountingDTO.class);
    }


    public List<AccountingDTO> listAccountingOrFilterByDifferentParameters(Map<String, String> params) {
        List<Accounting> accountings = new ArrayList<>();
        Type targetListType = new TypeToken<List<AccountingDTO>>() {}.getType();
        if (params == null || params.isEmpty()) {
            accountings = accountingRepository.findAll();
            return modelMapper.map(accountings, targetListType);
        }
        if (params.containsKey("accountingDate")) {
            accountings = accountingRepository.findAllByAccountingDate(LocalDate.parse(params.get("accountingDate")));
        } else if (params.containsKey("employeeId")) {
            accountings = accountingRepository.findAllByEmployeeId(params.get("employeeId"));
        } else if (params.containsKey("employeeLastName")) {
            accountings = accountingRepository.findAllByEmployeeLastName(params.get("employeeLastName"));
        } else if (params.containsKey("invoiceStatus")) {
            accountings = accountingRepository.findAllByInvoiceStatus(InvoiceStatus.valueOf(params.get("invoiceStatus")));
        } else if (params.containsKey("apInvoiceId")) {
            accountings = accountingRepository.findAllByApInvoiceId(params.get("apInvoiceId"));
        }
        return modelMapper.map(accountings, targetListType);
    }
}
