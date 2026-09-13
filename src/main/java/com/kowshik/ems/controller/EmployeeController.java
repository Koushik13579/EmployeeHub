package com.kowshik.ems.controller;

import com.kowshik.ems.entity.Employee;
import com.kowshik.ems.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/employees";
    }

    @GetMapping("/employees/export")
    public void exportEmployees(HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=employees.csv"
        );

        PrintWriter writer = response.getWriter();

        writer.println("ID,First Name,Last Name,Email,Phone,Department,Position,Joining Date");

        List<Employee> employees = employeeService.getAllEmployees();

        for (Employee employee : employees) {

            writer.println(
                    employee.getId() + "," +
                            employee.getFirstName() + "," +
                            employee.getLastName() + "," +
                            employee.getEmail() + "," +
                            employee.getPhoneNumber() + "," +
                            employee.getDepartment() + "," +
                            employee.getPosition() + "," +
                            employee.getJoiningDate()
            );
        }

        writer.flush();
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute(
                "totalEmployees",
                employeeService.getAllEmployees().size()
        );

        model.addAttribute(
                "departmentStats",
                employeeService.getEmployeeCountByDepartment()
        );

        model.addAttribute(
                "recentEmployees",
                employeeService.getRecentEmployees()
        );

        return "dashboard";
    }

    @GetMapping("/employees")
    public String getAllEmployees(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, 5, sort);

        Page<Employee> employeePage;

        if (keyword != null && !keyword.isBlank()
                && department != null && !department.isBlank()) {

            employeePage = employeeService.searchByKeywordAndDepartment(
                    keyword,
                    department,
                    pageable
            );

        } else if (department != null && !department.isBlank()) {

            employeePage = employeeService.getEmployeesByDepartment(
                    department,
                    pageable
            );

        } else if (keyword != null && !keyword.isBlank()) {

            employeePage = employeeService.searchEmployees(
                    keyword,
                    pageable
            );

        } else {

            employeePage = employeeService.getEmployees(pageable);
        }

        model.addAttribute("employees", employeePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeePage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("department", department);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        return "employees";
    }

    @GetMapping("/employees/new")
    public String showAddEmployeeForm(Model model) {

        model.addAttribute("employee", new Employee());

        return "add-employee";
    }

    @PostMapping("/employees")
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult result, RedirectAttributes redirectAttributes) {
        boolean isNewEmployee = employee.getId() == null;
        if (employee.getId() == null) {

            if (employeeService.isEmailAlreadyExists(employee.getEmail())) {
                result.rejectValue(
                        "email",
                        "error.employee",
                        "Email already exists"
                );
            }

        } else {

            // Updating an existing employee
            if (employeeService.isEmailAlreadyExistsForAnotherEmployee(
                    employee.getEmail(),
                    employee.getId())) {

                result.rejectValue(
                        "email",
                        "error.employee",
                        "Email already exists"
                );
            }
        }

        if (result.hasErrors()) {

            // Important: return the correct page
            if (employee.getId() == null) {
                return "add-employee";
            } else {
                return "edit-employee";
            }
        }

        employeeService.saveEmployee(employee);

        if (isNewEmployee) {
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Employee added successfully!"
            );
        } else {
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Employee updated successfully!"
            );
        }

        return "redirect:/employees";
    }

    @GetMapping("/employees/edit/{id}")
    public String showEditEmployeeForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);

        return "edit-employee";
    }

    @GetMapping("/employees/view/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return "view-employee";
    }

    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employeeService.deleteEmployeeById(id);
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Employee deleted successfully!"
        );

        return "redirect:/employees";
    }
}
