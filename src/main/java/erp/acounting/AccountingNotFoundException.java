package erp.acounting;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class AccountingNotFoundException extends AbstractThrowableProblem {

    private static final URI TYPE = URI.create("accountings/accounting-not-found");

    public AccountingNotFoundException(String id) {
        super(  TYPE,
                "Not found",
                Status.NOT_FOUND,
                String.format("Accounting not found: %s", id));
    }
}
