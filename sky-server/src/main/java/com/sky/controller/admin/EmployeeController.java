package com.sky.controller.admin;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    // 构造方法注入
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // 登录请求
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        return Result.success(employeeService.login(employeeLoginDTO));
    }

    // 修改密码请求
    @PutMapping("/editPassword")
    public Result<Object> editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        employeeService.editPassword(passwordEditDTO);
        return Result.success();
    }

    // 退出登录请求
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    // 员工状态更改请求
    @PostMapping("/status/{status}")
    public Result<Object> updateEmployeeStatus(@PathVariable("status") Integer newStatus, @RequestParam("id") Integer id) {
        employeeService.updateEmployeeStatus(id, newStatus);
        return Result.success();
    }

    // 员工分页查询
    @GetMapping("/page")
    public Result<PageResult<Employee>> page(@RequestParam Integer page,
                                   @RequestParam Integer pageSize,
                                   String name) {
        PageResult<Employee> pageResult = employeeService.page(page, pageSize, name);
        return Result.success(pageResult);
    }

    // 新增员工
    @PostMapping
    public Result<Object> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.addEmployee(employeeDTO);
        return Result.success();
    }

    // 根据ID查询员工
    @GetMapping("/{id}")
    public Result<Employee> getEmployeeById(@PathVariable("id") Integer id) {
        Employee employee = employeeService.getEmployeeById(id);
        return Result.success(employee);
    }

    // 编辑员工信息
    @PutMapping
    public Result<Object> updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.editEmployeeInformation(employeeDTO);
        return Result.success();
    }
}
