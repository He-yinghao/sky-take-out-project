package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    // 根据用户名查询用户
    @Select("select * from employee where username=#{username}")
    Employee getByUserName(String username);

    // 根据ID查询密码
    @Select("select password from employee where id=#{id}")
    String getPasswordById(Integer id);

    // 根据ID修改密码
    @AutoFill(OperationType.UPDATE)
    @Update("update employee set password=#{password},update_time=#{updateTime}, update_user=#{updateUser} where id=#{id}")
    void editPasswordByID(Employee employee);

    // 根据ID修改员工状态
    @AutoFill(OperationType.UPDATE)
    @Update("update employee set status=#{status},update_time=#{updateTime}, update_user=#{updateUser} where id=#{id}")
    void updateEmployeeStatusByID(Employee employee);


    // 分页查询符合条件的所有员工
    List<Employee> pageConditionalSelectAll(String name);

    // 新增员工
    @AutoFill(OperationType.INSERT)
    @Insert("insert into employee (name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "values (#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void addEmployee(Employee employee);

    // 根据ID查询员工
    @Select("select * from employee where id=#{id}")
    Employee getEmployeeById(Integer id);

    // 修改员工信息
    @AutoFill(OperationType.UPDATE)
    @Update("update employee set name=#{name}, username=#{username}, phone=#{phone}, sex=#{sex}, id_number=#{idNumber}, update_time=#{updateTime}, update_user=#{updateUser} where id=#{id}")
    void editEmployeeInformation(Employee employee);
}
