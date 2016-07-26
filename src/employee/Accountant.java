package employee;

import data.EmployeeType;
import data.SalaryType;

/**
 * Created by Alenka on 20.07.2016.
 */
public class Accountant extends Employee {

    private static double salary = 5000;
    private static SalaryType salaryType = SalaryType.FixedSalary;

    public Accountant() {
        super(salaryType, salary);
        setSetProfessions(EmployeeType.Accountant);
    }

    public static double getSalary() {
        return salary;
    }

    public static SalaryType getSalaryType() {
        return salaryType;
    }

}
