package erp.general;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class DuplicatedDataException extends AbstractThrowableProblem {

    public DuplicatedDataException(String message) {
        super(URI.create("partners/duplicated-data"),
                "Duplicated Data",
                Status.BAD_REQUEST,
                message);
    }


}
