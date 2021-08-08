package erp.acounting;

import erp.apinvoice.APInvoice;
import erp.apinvoice.APInvoiceDTO;
import erp.apinvoice.InvoiceStatus;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

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

    public List<AccountingDTO> listAccountingOrFilterByInvoiceId(Optional<String> invoiceid) {
        List<Accounting> accountings;
        if (invoiceid.isEmpty()) {
            accountings = accountingRepository.findAll();
        } else {
            accountings = accountingRepository.findAllByApInvoiceId(invoiceid.get());
        }
        Type targetListType = new TypeToken<List<AccountingDTO>>() {}.getType();
        return modelMapper.map(accountings, targetListType);
    }

    public List<AccountingDTO> listByInvoiceStatus(InvoiceStatus invoiceStatus) {
        List<Accounting> accountings = accountingRepository.findAllByInvoiceStatus(invoiceStatus);
        Type targetListType = new TypeToken<List<AccountingDTO>>() {}.getType();
        return modelMapper.map(accountings, targetListType);
    }


}
