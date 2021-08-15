package erp.accounting;

import erp.apinvoice.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountingRepository extends JpaRepository<Accounting, String> {

    List<Accounting> findAllByApInvoiceId(String invoiceId);

    List<Accounting> findAllByInvoiceStatus(InvoiceStatus invoiceStatus);
}
