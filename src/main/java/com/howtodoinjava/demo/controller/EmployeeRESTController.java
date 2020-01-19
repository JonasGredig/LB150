package com.howtodoinjava.demo.controller;

import com.howtodoinjava.demo.exception.RecordNotFoundException;
import com.howtodoinjava.demo.model.Employee;
import com.howtodoinjava.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(value = "/employee-management", produces = {MediaType.APPLICATION_JSON_VALUE})
@CrossOrigin
@Validated
public class EmployeeRESTController {
    @Autowired
    private EmployeeRepository repository;

    public EmployeeRepository getRepository() {
        return repository;
    }

    public void setRepository(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/employees")
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }


    @PostMapping("/employees")
    Employee createOrSaveEmployee(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }

    @GetMapping("/employee/vote/{id}")
    Employee voteEmployee(@PathVariable int id) {
        Employee employee = getEmployeeById(id + 0l);
        employee.setVotes(employee.getVotes() + 1);
        return repository.save(employee);
    }

    @GetMapping("/employees/{id}")
    Employee getEmployeeById(@PathVariable
                             @Min(value = 1, message = "id must be greater than or equal to 1")
                             @Max(value = 1000, message = "id must be lower than or equal to 1000") Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Employee id '" + id + "' does no exist"));
    }

    @PutMapping("/employees/{id}")
    Employee updateEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

        return repository.findById(id).map(employee -> {
            employee.setFirstName(newEmployee.getFirstName());
            employee.setLastName(newEmployee.getLastName());
            employee.setEmail(newEmployee.getEmail());
            employee.setDepartmentId(newEmployee.getDepartmentId());
            employee.setVotes(newEmployee.getVotes());
            return repository.save(employee);
        }).orElseGet(() -> {
            newEmployee.setId(id);
            return repository.save(newEmployee);
        });
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
