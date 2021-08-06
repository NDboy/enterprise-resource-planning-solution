package erp.employee;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    private ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }



    public EmployeeDTO createEmployee(CreateEmployeeCommand command) {
        Employee employee = new Employee(command.getFirstName(), command.getLastName(), command.getStatus(), command.getAddress(), command.getEntryDate());
        employeeRepository.save(employee);
        EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);
        return employeeDTO;
    }

//    public List<EmployeeDTO> listEmployees() {
//        List<Employee> employees = employeeRepository.findAll();
//        Type targetListType = new TypeToken<List<EmployeeDTO>>() {}.getType();
//        return modelMapper.map(employees, targetListType);
//    }

    public EmployeeDTO findEmployeeById(String id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    public List<EmployeeDTO> listEmployeesByStatus(Optional<EmployeeStatus> status) {
        List<Employee> employees;

        if (status.isEmpty()) {
            employees = employeeRepository.findAll();
        } else {
            employees = employeeRepository.findAllByStatus(status.orElseThrow(() -> new EmployeeNotFoundException("Employee not found with this status:" + status)));
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




}
