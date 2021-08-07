package erp.partner;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class PartnerNotFoundException extends AbstractThrowableProblem {

    private static final URI TYPE = URI.create("partners/partner-not-found");

    public PartnerNotFoundException(String id) {
        super(  TYPE,
                "Not found",
                Status.NOT_FOUND,
                String.format("Partner not found: %s", id));
    }
}
