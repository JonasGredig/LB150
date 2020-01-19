package com.howtodoinjava.demo.controller;

import com.howtodoinjava.demo.exception.RecordNotFoundException;
import com.howtodoinjava.demo.model.Department;
import com.howtodoinjava.demo.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(value = "/department-management", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class DepartmentRESTController {

    @Autowired
    private DepartmentRepository repository;

    public DepartmentRepository getRepository() {
        return repository;
    }

    public void setRepository(DepartmentRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/departments")
    public List<Department> getAllDepartments() {
        return repository.findAll();
    }


    @PostMapping("/departments")
    Department createOrSaveDepartment(@RequestBody Department newDepartment) {
        return repository.save(newDepartment);
    }


    @GetMapping("/departments/{id}")
    Department getDepartmentById(@PathVariable
                             @Min(value = 1, message = "id must be greater than or equal to 1")
                             @Max(value = 1000, message = "id must be lower than or equal to 1000") Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Employee id '" + id + "' does no exist"));
    }

    @PutMapping("/departments/{id}")
    Department updateDepartment(@RequestBody Department newDepartment, @PathVariable Long id) {

        return repository.findById(id).map(department -> {
            department.setDivision(newDepartment.getDivision());
            department.setName(newDepartment.getName());
            return repository.save(department);
        }).orElseGet(() -> {
            newDepartment.setId(id);
            return repository.save(newDepartment);
        });
    }

    @DeleteMapping("/departments/{id}")
    void deleteDepartment(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
