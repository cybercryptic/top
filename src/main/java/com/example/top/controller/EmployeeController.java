package com.example.top.controller;

import com.example.top.dto.employee.EmployeeDto;
import com.example.top.dto.employee.UpdateAccountDto;
import com.example.top.entity.employee.Account;
import com.example.top.entity.employee.Employee;
import com.example.top.service.EmployeeService;
import com.example.top.service.RoleService;
import com.example.top.util.mapper.EmployeeMapper;
import com.example.top.util.mapper.Mapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/employees")
@PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
public class EmployeeController extends AController {

    @Autowired
    private EmployeeService empService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/new-employee")
    public ModelAndView renderAddEmployee() {
        return getAddView(new Employee());
    }

    @GetMapping("/update-employee")
    public ModelAndView renderUpdateEmployeeInfo(Long id) {
        return getUpdateEmployeeView(empService.getEmployee(id));
    }

    @GetMapping("/update-emp-account")
    public ModelAndView renderUpdateEmployeeAccount(Long id) {
        var mv = new ModelAndView();
        mv.addObject("account", empService.getAccount(id));
        mv.addObject("employeeId", id);
        mv.setViewName("employee/save-emp-account");

        return mv;
    }

    @PostMapping("/save-employee")
    public ModelAndView saveEmployee(@Valid @ModelAttribute("employee") EmployeeDto employee, BindingResult bindingResult,
                                     RedirectAttributes attributes) {
        if (bindingResult.hasErrors() && !isAccountNull(employee, bindingResult)) return getAddView(employee);

        empService.saveEmployee(EmployeeMapper.map(employee));

        var message = "";
        if (employee.getEmployeeId() == null) message = "New employee '"+ employee.getFullName() +"' has been successfully saved!";
        else message = "Employee '"+ employee.getFullName() +"' has been successfully updated!";
        attributes.addFlashAttribute("alertMessage", message);
        return new ModelAndView("redirect:view");
    }

    private boolean isAccountNull(EmployeeDto employee, BindingResult bindingResult) {
        return !employee.isHasAccount() && areThereOtherErrorsThenAccountErrors(bindingResult);
    }

    private boolean areThereOtherErrorsThenAccountErrors(BindingResult bindingResult) {
        return noOfIgnoreErrors(bindingResult) == noOfErrors(bindingResult);
    }

    private int noOfIgnoreErrors(BindingResult bindingResult) {
        var no = 0;
        if (bindingResult.hasFieldErrors("account.username")) no++;
        if (bindingResult.hasFieldErrors("account.password")) no++;

        return no;
    }

    private int noOfErrors(BindingResult bindingResult) {
        return bindingResult.getAllErrors().size();
    }

    private ModelAndView getAddView(Object employee) {
        var mv = new ModelAndView();
        mv.addObject("employee", employee);
        mv.addObject("roles", roleService.findAllRoles());
        mv.setViewName("employee/save-employee");

        return mv;
    }

    @PostMapping("/save-emp-info")
    public ModelAndView saveEmployeeInfo(@Valid @ModelAttribute("employee") EmployeeDto employee, BindingResult bindingResult,
                                         RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) return getUpdateEmployeeView(employee);

        empService.saveEmployee(EmployeeMapper.mapInfo(employee));

        var message = "Employee '"+ employee.getFullName() +"' has been successfully updated!";
        attributes.addFlashAttribute("alertMessage", message);
        return new ModelAndView("redirect:view");
    }

    private ModelAndView getUpdateEmployeeView(Object employee) {
        var mv = new ModelAndView();
        mv.addObject("employee",  employee);
        mv.addObject("roles", roleService.findAllRoles());
        mv.setViewName("employee/save-emp-info");

        return mv;
    }

    @PostMapping("/save-emp-account")
    public ModelAndView saveAccount(@Valid @ModelAttribute("account") UpdateAccountDto account, BindingResult bindingResult,
                                    RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            var mv = new ModelAndView();
            mv.addObject("account", account);
            mv.setViewName("employee/save-emp-account");

            return mv;
        }

        empService.saveAccount(account.getEmployeeId(), Mapper.map(account, new Account()));

        var message = "";
        if (account.getAccountId() == null) message = "Account successfully created for username: '"+ account.getUsername() +"'";
        else message = "Account details updated for username: '"+ account.getUsername() +"'";
        attributes.addFlashAttribute("alertMessage", message);
        return new ModelAndView("redirect:view");
    }

    @RequestMapping("/view")
    public ModelAndView getEmployees(@RequestParam(defaultValue = "0") int page) {
        var mv = new ModelAndView();
        mv.addObject("employees", empService.findAllEmployees(page));
        mv.addObject("currentPage", page);
        mv.setViewName("employee/employee");

        return mv;
    }

    @GetMapping("/delete-employee")
    public RedirectView deleteEmployee(Long id, RedirectAttributes attributes) {
        var employee = empService.deleteEmployee(id);

        var message = "Employee '"+ employee.getFullName() +"' has been successfully deleted!";
        attributes.addFlashAttribute("alertMessage", message);
        return new RedirectView("view");
    }
}
