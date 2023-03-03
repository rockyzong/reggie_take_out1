package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")

    /**
     * login
     */
    public R<Employee> login(HttpServletRequest req, @RequestBody Employee employee) {

        /*Md5 加密*/

        String password = employee.getPassword();

        password = DigestUtils.md5DigestAsHex(password.getBytes());

        /*查询数据库*/

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Employee::getUsername, employee.getUsername());

        Employee emp = employeeService.getOne(queryWrapper);

        /*用户名错误*/
        if (emp == null) {

            return R.error("user not exist");
        }

        if (!emp.getPassword().equals(password)) {

            return R.error("password incorrect");
        }

        if (emp.getStatus() == 0) {

            return R.error("user not valida");
        }


        req.getSession().setAttribute("employee", emp.getId());

        return R.success(emp);


    }

    /**
     * exit
     */

    @PostMapping("/logout")
        public R<String> exit(HttpServletRequest req) {

        req.getSession().removeAttribute("employee");

        return R.success("Exit Successful");
    }






}
