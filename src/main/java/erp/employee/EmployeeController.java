package erp.employee;

import erp.employee.dto.UpdateEmployeeCommand;
import erp.partner.dto.UpdatePartnerCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(exposedHeaders="Access-Control-Allow-Origin")
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

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "find employee by id and update",
            description = "find employee by id and update")
    public EmployeeDTO updateEmployee(@PathVariable("id") String id, @Valid @RequestBody UpdateEmployeeCommand command) {
        return employeeService.updateEmployee(id, command);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Employee by id without APInvoice", description = "Delete Employee by id without APInvoice")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployeeById(@Parameter(example = "ADO-00001") @PathVariable("id") String id) {
        employeeService.deleteEmployeeById(id);
    }


}
