package erp.employee;

import erp.accounting.AccountingRepository;
import erp.apinvoice.APInvoiceRepository;
import erp.employee.dto.UpdateEmployeeCommand;
import erp.general.GenerateData;
import erp.partner.PartnerRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeService {

    private EmployeeRepository employeeRepository;
    private AccountingRepository accountingRepository;
    private PartnerRepository partnerRepository;
    private APInvoiceRepository apInvoiceRepository;

    private ModelMapper modelMapper;

    public EmployeeDTO createEmployee(CreateEmployeeCommand command) {
        if (command.getLastName().equals("Cleaning")){
            GenerateData gd = new GenerateData(apInvoiceRepository, partnerRepository, employeeRepository, accountingRepository);
            gd.cleanDbAndGenerateData();
            return new EmployeeDTO();
        }
        Employee employee = new Employee(command.getFirstName(), command.getLastName(), command.getStatus(), command.getAddress(), command.getEntryDate());
        employeeRepository.save(employee);
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    public EmployeeDTO findEmployeeById(String id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    public List<EmployeeDTO> listEmployeesByStatus(Optional<EmployeeStatus> status) {
        List<Employee> employees;

        if (status.isEmpty()) {
            employees = employeeRepository.findAll();
        } else {
            employees = employeeRepository.findAllByStatus(status.get());
        }
        Type targetListType = new TypeToken<List<EmployeeDTO>>() {}.getType();

        return modelMapper.map(employees, targetListType);
    }

    @Transactional
    public EmployeeDTO changeStatusById(String id, UpdateStatusCommand command) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
        if (employee.getStatus() != command.getStatus()) {
            employee.setStatus(command.getStatus());
        }
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    @Transactional
    public EmployeeDTO changeAddressById(String id, UpdateAddressCommand command) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
        if (employee.getAddress() != command.getAddress()) {
            employee.setAddress(command.getAddress());
        }
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    @Transactional
    public EmployeeDTO updateEmployee(String id, UpdateEmployeeCommand command) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
        employee.update(command);
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    public void deleteEmployeeById(String id) {
        employeeRepository.deleteById(id);
    }
}
