package com.kowshik.ems.service;

import com.kowshik.ems.entity.Employee;
import com.kowshik.ems.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kowshik.ems.exception.EmployeeNotFoundException;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getRecentEmployees() {
        return employeeRepository.findTop5ByOrderByIdDesc();
    }

    // Get all employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Page<Employee> getEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public Page<Employee> searchByKeywordAndDepartment(
            String keyword,
            String department,
            Pageable pageable) {

        return employeeRepository.searchByKeywordAndDepartment(
                keyword,
                department,
                pageable
        );
    }

    public Page<Employee> searchEmployees(
            String keyword,
            Pageable pageable) {

        return employeeRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword,
                        pageable
                );
    }

    // Add a new employee
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found with ID: " + id)
                );
    }

    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }

    public boolean isEmailAlreadyExists(String email) {
        return employeeRepository.existsByEmail(email);
    }

    public boolean isEmailAlreadyExistsForAnotherEmployee(String email, Long id) {
        return employeeRepository.existsByEmailAndIdNot(email, id);
    }

    public List<Employee> searchEmployees(String keyword) {

        return employeeRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword
                );
    }

    public Page<Employee> getEmployeesByDepartment(
            String department,
            Pageable pageable) {

        return employeeRepository.findByDepartment(
                department,
                pageable
        );
    }

    public List<Object[]> getEmployeeCountByDepartment() {
        return employeeRepository.countEmployeesByDepartment();
    }
}
