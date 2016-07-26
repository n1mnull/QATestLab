package data;

/**
 * Created by Alenka on 20.07.2016.
 */
public enum EmployeeType {
    Director("Director", employee.Director.getSalary(), employee.Director.getSalaryType()),
    Accountant("Accountant", employee.Accountant.getSalary(), employee.Accountant.getSalaryType()),
    Manager("Manager", employee.Manager.getSalary(), employee.Manager.getSalaryType()),
    Cleaner("Cleaner", employee.Cleaner.getSalary(), employee.Cleaner.getSalaryType()),
    Programmer("Programmer", employee.Programmer.getSalary(), employee.Programmer.getSalaryType()),
    Designer("Designer", employee.Designer.getSalary(), employee.Designer.getSalaryType()),
    Tester("Tester", employee.Tester.getSalary(), employee.Tester.getSalaryType()),
    Freelancer("Freelancer", employee.Freelancer.getSalary(), employee.Freelancer.getSalaryType());

    private String employeeTypeName;
    private double employeeSalary;
    private SalaryType salaryType;

    EmployeeType(String employeeTypeName, double employeeSalary, SalaryType salaryType) {
        this.employeeTypeName = employeeTypeName;
        this.employeeSalary = employeeSalary;
        this.salaryType= salaryType;
    }

    public double getEmployeeSalary() {
        return employeeSalary;
    }

    public SalaryType getSalaryType() {
        return salaryType;
    }

    public String getEmployeeTypeName() {
        return employeeTypeName;
    }
}
