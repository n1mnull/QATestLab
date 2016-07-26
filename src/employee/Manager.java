package employee;

import data.EmployeeType;
import data.SalaryType;

/**
 * Created by Alenka on 20.07.2016.
 */
public class Manager extends Employee {

    private static double salary = 4000;
    private static SalaryType salaryType = SalaryType.FixedSalary;

    public Manager() {
        super(salaryType, salary);
        setSetProfessions(EmployeeType.Manager);
    }

    public static double getSalary() {
        return salary;
    }

    public static SalaryType getSalaryType() {
        return salaryType;
    }

}
