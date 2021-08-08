package erp.apinvoice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface APInvoiceRepository extends JpaRepository<APInvoice, String> {

    List<APInvoice> findAllByPartnerId(String id);

    List<APInvoice> findAllByInvoiceStatus(InvoiceStatus invoiceStatus);
}
