package com.sparta.burgerspring.controller;


import com.sparta.burgerspring.model.entities.*;
import com.sparta.burgerspring.model.repositories.DeptManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class DeptManagerWebController {

    private DeptManagerRepository deptManagerRepository;

    @Autowired
    public DeptManagerWebController(DeptManagerRepository deptManagerRepository){
        this.deptManagerRepository = deptManagerRepository;
    }

    @GetMapping("/department-managers")
    public String getTitle(Model model) {
        model.addAttribute("managers", deptManagerRepository.findAll());
        return "manager/manager";
    }

    @GetMapping("/department-managers/create")
    public String getDeptMgrToCreate() {
        return "manager/manager-create-form";
    }

    @PostMapping("/createManager")
    public String createManager(@ModelAttribute("deptMgrToCreate")DeptManager newDeptManager, @ModelAttribute("deptMgrIdToCreate")DeptManagerId newDeptManagerId) {
        newDeptManager.setId(newDeptManagerId);
        if (newDeptManager.getToDate() == null) {
            newDeptManager.setToDate(LocalDate.of(9999, 01, 01));
        }
        deptManagerRepository.saveAndFlush(newDeptManager);
        return "manager/add-manager-success";
    }

    @PostMapping("/deleteMgr")
    public String findMgrToDelete(@ModelAttribute("mgrToDelete") DeptManagerId foundMgr) {
        return "redirect:/department-managers/delete/" + foundMgr.getEmpNo() + "/" + foundMgr.getDeptNo();
    }

    @GetMapping("/department-managers/delete/{id}/{deptNo}")
    public String deleteMgr(@PathVariable Integer id, @PathVariable String deptNo) {
        DeptManagerId deptManagerId = new DeptManagerId();
        deptManagerId.setEmpNo(id);
        deptManagerId.setDeptNo(deptNo);
        deptManagerRepository.deleteById(deptManagerId);
        return "manager/manager-delete-success";
    }

    @GetMapping("/department-managers/edit/{id}/{deptNo}")
    public String getMgrToEdit(@PathVariable Integer id, @PathVariable String deptNo, Model model) {
        DeptManagerId deptManagerId = new DeptManagerId();
        deptManagerId.setEmpNo(id);
        deptManagerId.setDeptNo(deptNo);
        DeptManager deptManager = deptManagerRepository.findById(deptManagerId).orElse(null);
        model.addAttribute("mgrToEdit", deptManager);
        return "manager/manager-edit-form";
    }
    @PostMapping("/updateManager")
    public String updateManager(@ModelAttribute("mgrToEdit")DeptManager editedMgr) {
        if(editedMgr.getToDate() == null){
            editedMgr.setToDate(LocalDate.of(9999, 01, 01));
        }
        deptManagerRepository.saveAndFlush(editedMgr);
        return "manager/manager-edit-success";
    }


}
