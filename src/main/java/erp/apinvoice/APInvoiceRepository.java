package erp.apinvoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;

public interface APInvoiceRepository extends JpaRepository<APInvoice, String> {


    @Query("select a from APInvoice a where a.invoiceStatus = :invoiceStatus")
    List<APInvoice> findAllByInvoiceStatus(@Param("invoiceStatus") InvoiceStatus invoiceStatus);

    @Query("select a from APInvoice a where upper(a.invNum) = upper(?1)")
    List<APInvoice> findByInvNumEqualsIgnoreCase(@NonNull String invNum);

    @Query("select a from APInvoice a where a.paymentModeAndDates.paymentMode = :paymentMode")
    List<APInvoice> findByPaymentMode(PaymentMode paymentMode);

    @Query("select a from APInvoice a where a.paymentModeAndDates.dueDate <= :dueDate")
    List<APInvoice> findByDueDateIsBefore(@NonNull LocalDate dueDate);

    @Query("select a from APInvoice a join fetch a.partner p where p.id = :partnerId")
    List<APInvoice> findByPartnerId(@NonNull String partnerId);

    @Query("select a from APInvoice a join fetch a.employee e where e.id = :employeeId")
    List<APInvoice> findByEmployeeId(@NonNull String employeeId);

    @Query("select a from APInvoice a left join fetch a.accountings accountings where accountings.accountingDate >= :accountingDate")
    List<APInvoice> findByAccountingDateIsAfter(@NonNull LocalDate accountingDate);














}
