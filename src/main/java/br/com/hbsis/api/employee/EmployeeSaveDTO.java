package br.com.hbsis.api.employee;

import org.springframework.stereotype.Component;

@Component
public class EmployeeSaveDTO {
    private String employeeNome;
    private String employeeUuid;

    public EmployeeSaveDTO(){
    }

    public EmployeeSaveDTO(String employeeNome, String employeeUuid){
        this.employeeNome = employeeNome;
        this.employeeUuid = employeeUuid;
    }

    public String getEmployeeNome() { return employeeNome; }

    public void setEmployeeNome(String employeeNome) {
        this.employeeNome = employeeNome;
    }

    public String getEmployeeUuid() {
        return employeeUuid;
    }

    public void setEmployeeUuid(String employeeUuid) {
        this.employeeUuid = employeeUuid;
    }
}