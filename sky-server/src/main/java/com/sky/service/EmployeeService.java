package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.vo.EmployeeLoginVO;

//接口中声明方法
public interface EmployeeService {
    EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO);

    void editPassword(PasswordEditDTO passwordEditDTO);

    void updateEmployeeStatus(Integer id, Integer newStatus);

    PageResult<Employee> page(Integer page, Integer pageSize, String name);

    void addEmployee(EmployeeDTO employeeDTO);

    Employee getEmployeeById(Integer id);

    void editEmployeeInformation(EmployeeDTO employeeDTO);

}
