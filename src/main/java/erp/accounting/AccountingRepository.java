package erp.accounting;

import erp.apinvoice.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AccountingRepository extends JpaRepository<Accounting, String> {

    List<Accounting> findAllByApInvoiceId(String invoiceId);

    List<Accounting> findAllByInvoiceStatus(InvoiceStatus invoiceStatus);

    List<Accounting> findAllByAccountingDate(LocalDate accountingDate);

    List<Accounting> findAllByEmployeeId(String employeeId);

    @Query("select a from Accounting a join fetch a.employee e where e.lastName = :employeeLastName")
    List<Accounting> findAllByEmployeeLastName(String employeeLastName);
}
