package com.sparta.burgerspring.controller;

import com.sparta.burgerspring.BurgerSpringApplication;
import com.sparta.burgerspring.model.entities.*;
import com.sparta.burgerspring.model.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class EmployeeCreateWebController {

    private EmployeeRepository employeeRepository;
    private SalaryRepository salaryRepository;
    private DeptEmpRepository deptEmpRepository;
    private TitleRepository titleRepository;
    private DepartmentRepository departmentRepository;

    public EmployeeCreateWebController(EmployeeRepository employeeRepository, SalaryRepository salaryRepository, DeptEmpRepository deptEmpRepository, TitleRepository titleRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.salaryRepository = salaryRepository;
        this.deptEmpRepository = deptEmpRepository;
        this.titleRepository = titleRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping("/employeecreation")
    public String getEmployeeCreationform(Model model){
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("titles", titleRepository.findAllTitles());
        return "employeecreation/employee-create-form";
    }

    @PostMapping("/employeecreationform")
    public String createEmployee(@ModelAttribute("employeeToCreate")EmployeeCreation employeeCreation, Model model) {
        Employee addedEmployee = new Employee();
        Salary addedSalary = new Salary();
        SalaryId addedSalaryId = new SalaryId();
        Title addedTitle = new Title();
        TitleId addedTitleId = new TitleId();
        DeptEmpId addedDeptEmpId = new DeptEmpId();
        DeptEmp addedDeptEmp = new DeptEmp();
        addedEmployee.setId(employeeCreation.getId());
        addedEmployee.setGender(employeeCreation.getGender());
        addedEmployee.setBirthDate(employeeCreation.getBirthDate());
        addedEmployee.setFirstName(employeeCreation.getFirstName());
        addedEmployee.setLastName(employeeCreation.getLastName());
        addedEmployee.setHireDate(employeeCreation.getHireDate());
        employeeRepository.save(addedEmployee);
        addedSalaryId.setEmpNo(employeeCreation.getId());
        addedSalaryId.setFromDate(employeeCreation.getHireDate());
        addedSalary.setId(addedSalaryId);
        addedSalary.setEmpNo(addedEmployee);
        addedSalary.setToDate(LocalDate.of(9999,01,01));
        addedSalary.setSalary(employeeCreation.getSalary());
        addedTitleId.setTitle(employeeCreation.getTitle());
        addedTitleId.setFromDate(employeeCreation.getHireDate());
        addedTitleId.setEmpNo(employeeCreation.getId());
        addedTitle.setId(addedTitleId);
        addedTitle.setToDate(LocalDate.of(9999,01,01));
        addedTitle.setEmpNo(addedEmployee);
        addedDeptEmpId.setDeptNo(employeeCreation.getDeptName());
        addedDeptEmpId.setEmpNo(employeeCreation.getId());
        addedDeptEmp.setId(addedDeptEmpId);
        BurgerSpringApplication.logger.info(employeeCreation.toString());
        addedDeptEmp.setDeptNo(departmentRepository.findById(employeeCreation.getDeptName()).orElse(null));
        addedDeptEmp.setFromDate(employeeCreation.getHireDate());
        addedDeptEmp.setToDate(LocalDate.of(9999,01,01));
        model.addAttribute("salary", addedSalary);
        model.addAttribute("title", addedTitle);
        model.addAttribute("deptEmp", addedDeptEmp);
//        salaryRepository.save(addedSalary);
//        titleRepository.save(addedTitle);
//        deptEmpRepository.save(addedDeptEmp);
        return "employeecreation/employee-create-checkout";
    }

    @PostMapping("/employeecreationcheckout")
    public String checkoutEmployee(){

    }
}
