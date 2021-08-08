package erp.apinvoice;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class APInvoiceNotFoundException extends AbstractThrowableProblem {

    private static final URI TYPE = URI.create("apinvoices/apinvoice-not-found");

    public APInvoiceNotFoundException(String id) {
        super(  TYPE,
                "Not found",
                Status.NOT_FOUND,
                String.format("A/P invoice not found: %s", id));
    }



}
