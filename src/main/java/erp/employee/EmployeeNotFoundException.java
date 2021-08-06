package erp.employee;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class EmployeeNotFoundException extends AbstractThrowableProblem {

    private static final URI TYPE = URI.create("employees/employee-not-found");

    public EmployeeNotFoundException(String id) {
        super(  TYPE,
                "Not found",
                Status.NOT_FOUND,
                String.format("Employee with id: %s not found", id));
    }
}
