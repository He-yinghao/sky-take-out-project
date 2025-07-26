package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordEditFailedException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.utils.MD5Util;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//接口中声明方法
@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeMapper employeeMapper;
    private final JwtProperties jwtProperties;

    public EmployeeServiceImpl(EmployeeMapper employeeMapper, JwtProperties jwtProperties) {
        this.employeeMapper = employeeMapper;
        this.jwtProperties = jwtProperties;
    }


    @Override
    public EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO) {
        // 1.根据用户名查询用户信息
        Employee employee = employeeMapper.getByUserName(employeeLoginDTO.getUsername());
        // 2.查看当前账号是否存在
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 3.判断账号输入密码是否正确
        if (!employee.getPassword().equals(MD5Util.md5(employeeLoginDTO.getPassword()))) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        // 4.查看当前账号是否被锁定
        if (Objects.equals(employee.getStatus(), StatusConstant.DISABLE)) {
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        // 5.满足上述所有条件下生成 jwt token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.generateJWT(jwtProperties.getAdminkey(),
                jwtProperties.getAdminexpire(),
                claims);
        // 6.将查询得到的数据按照VO格式响应前端
        return EmployeeLoginVO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .username(employee.getUsername())
                .token(token)
                .build();
    }

    @Override
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        Integer id = BaseContext.getCurrentId();
        String oldPassword = MD5Util.md5(passwordEditDTO.getOldPassword());
        String newPassword = MD5Util.md5(passwordEditDTO.getNewPassword());
        String currentActualPassword = employeeMapper.getPasswordById(id);

        if (currentActualPassword.equals(oldPassword)) {
            if (oldPassword.equals(newPassword)) {
                throw new PasswordEditFailedException(MessageConstant.PASSWORD_MUST_DIFFER_ERROR);
            }
            Employee employee = Employee.builder()
                    .id(id)
                    .password(newPassword)
                    .build();
            employeeMapper.editPasswordByID(employee);
        } else {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
    }

    @Override
    public void updateEmployeeStatus(Integer id, Integer newStatus) {
        Employee employee = Employee.builder()
                .id(id)
                .status(newStatus)
                .build();
        employeeMapper.updateEmployeeStatusByID(employee);
    }

    /**
     * 分页查询员工信息，可以根据姓名进行模糊查询
     *
     * @param page     当前页码
     * @param pageSize 每页显示的记录数
     * @param name     员工姓名，模糊查询条件
     * @return 分页查询结果，包含总记录数和当前页的员工列表
     */
    @Override
    public PageResult<Employee> page(Integer page, Integer pageSize, String name) {
        // 设置分页参数
        PageHelper.startPage(page, pageSize);
        // 执行分页查询，获取员工列表
        List<Employee> employees = employeeMapper.pageConditionalSelectAll(name);
        // 将查询结果转换为Page对象
        Page<Employee> p = (Page<Employee>) employees;
        // 创建PageResult对象，返回分页查询结果
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //  拷贝属性
        BeanUtils.copyProperties(employeeDTO, employee);
        //  设置默认密码
        employee.setPassword(MD5Util.md5(PasswordConstant.DEFAULT_PASSWORD));
        //  设置默认状态
        employee.setStatus(StatusConstant.ENABLE);
        //  插入员工信息
        employeeMapper.addEmployee(employee);
    }

    @Override
    public Employee getEmployeeById(Integer id) {
        return employeeMapper.getEmployeeById(id);
    }

    @Override
    public void editEmployeeInformation(EmployeeDTO employeeDTO) {
        // 需要将更新该用户的管理员id保存
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employeeMapper.editEmployeeInformation(employee);
    }

}
