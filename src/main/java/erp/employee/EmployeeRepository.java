package erp.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    List<Employee> findAllByStatus(EmployeeStatus status);

}
