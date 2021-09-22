package erp.general;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class DataSavingException extends AbstractThrowableProblem {

    public DataSavingException(String message) {
        super(URI.create("partners/database-error"),
                "Database Error",
                Status.BAD_REQUEST,
                message);
    }
}
