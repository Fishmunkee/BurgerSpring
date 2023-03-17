package com.sparta.burgerspring.controller;

import com.sparta.burgerspring.model.entities.Department;
import com.sparta.burgerspring.model.entities.Employee;
import com.sparta.burgerspring.model.entities.Salary;
import com.sparta.burgerspring.model.repositories.DepartmentRepository;
import com.sparta.burgerspring.model.repositories.EmployeeRepository;
import com.sparta.burgerspring.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class DepartmentWebController {

    private DepartmentRepository departmentRepository;
    private DepartmentService departmentService;

    private EmployeeRepository employeeRepository;

    @Autowired
    public DepartmentWebController(DepartmentRepository departmentRepository, DepartmentService departmentService, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.departmentService = departmentService;
        this.employeeRepository = employeeRepository;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/departments")
    public String getAllDepartments(Model model){
        model.addAttribute("departments", departmentRepository.findAll());
        return "/departments/departments";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/departments/deptListByName/{firstName}/{lastName}")
    public String getDeptByEmployee(Model model, @PathVariable String firstName, @PathVariable String lastName) {
        model.addAttribute("departmentByEmployee", departmentService.getListOfDeptNamesByEmp(firstName, lastName));
        return "/departments/department-list-by-name";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/departments/findDepartment")
    public String getDeptToFind() {
        return "/departments/department-find-form";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/findDepartment")
    public String findDepartment(@ModelAttribute("departmentToFind") Employee foundEmployee) {
        departmentService.getListOfDeptNamesByEmp(foundEmployee.getFirstName(), foundEmployee.getLastName());
        String employee = foundEmployee.getFirstName() + "/" + foundEmployee.getLastName();
        return "redirect:/departments/deptListByName/" + employee;
    }


//    @GetMapping("departments/findAvgSalary")
//    public String getDeptSalToFind(Model model, @RequestParam String deptName, @RequestParam LocalDate date) {
//        String department = departmentService.getAvgSalByDeptNameAndDate(deptName, date);
//        model.addAttribute("deptSalToFind", department);
//        return "/departments/department-sal-by-name-date-form";
//    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("departments/findAvgSalary")
    public String getDeptSalAvgToFind() {
        return "/departments/department-sal-by-name-date-form";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/findAvgSalary")
    public String getAvgSalByDeptNameAndDate(@ModelAttribute("deptToFind") Department foundDepartment, LocalDate date) {
        departmentService.getAvgSalByDeptNameAndDate(foundDepartment.getDeptName(), date);
        String nameDate = foundDepartment.getDeptName() + "/" + date;
        return "redirect:/departments/avgSalByDeptNameAndDate/" + nameDate;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/departments/avgSalByDeptNameAndDate/{deptName}/{date}")
    public String getAvgSalByDeptNameAndDate(Model model, @PathVariable String deptName, @PathVariable LocalDate date) {
        model.addAttribute("avgSalByDept", departmentService.getAvgSalByDeptNameAndDate(deptName, date));
        return "/departments/department-sal-by-name-date";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/departments/create")
    public String deptToCreate() {
        return "/departments/department-create-form";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/createDepartment/")
    public String createDept(@ModelAttribute("deptToCreate") Department createdDepartment) {
        departmentRepository.saveAndFlush(createdDepartment);
        return "/departments/department-create-success";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/departments/edit/{id}")
    public String deptToEdit(Model model, @PathVariable String id) {
        Department department = departmentRepository.findDepartmentById(id);
        model.addAttribute("deptToEdit", department);
        return "/departments/department-edit-form";
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/editDepartment")
    public String editDept(@ModelAttribute("deptToEdit") Department editedDepartment) {
        departmentRepository.saveAndFlush(editedDepartment);
        return "/departments/department-edit-success";
    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("departments/delete/{id}")
    public String deleteDeptById(@PathVariable String id) {
        departmentRepository.deleteById(id);
        return "/departments/department-delete-success";
    }

//






}
