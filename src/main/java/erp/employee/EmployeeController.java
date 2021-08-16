package erp.employee;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@AllArgsConstructor
@Tag(name = "operations on employees")
public class EmployeeController {

    EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "create an employee", description = "create an employee")
    public EmployeeDTO createEmployee(@Valid @RequestBody CreateEmployeeCommand command) {
        return employeeService.createEmployee(command);
    }

    @GetMapping
    @Operation(summary = "list all employees or filter by status, (filtering syntax = /api/employees?status=ACTIVE)", description = "list all employees or filter employees by status")
    public List<EmployeeDTO> listEmployeesByStatus(@RequestParam Optional<EmployeeStatus> status) {
        return employeeService.listEmployeesByStatus(status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "find an employee by manually generated id", description = "find an employee by id")
    public EmployeeDTO findEmployeeById(@PathVariable("id") String id) {
        return employeeService.findEmployeeById(id);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "find employee by id and change the status", description = "find employee by id and change the status")
    public EmployeeDTO changeStatusById(@PathVariable("id") String id, @RequestBody UpdateStatusCommand command) {
        return employeeService.changeStatusById(id, command);
    }

    @PutMapping("/{id}/address")
    @Operation(summary = "find employee by id and change the address", description = "find employee by id and change the address")
    public EmployeeDTO changeAddressById(@PathVariable("id") String id, @Valid @RequestBody UpdateAddressCommand command) {
        return employeeService.changeAddressById(id, command);
    }


}
