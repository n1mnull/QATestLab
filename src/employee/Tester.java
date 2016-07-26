package employee;

import data.EmployeeType;
import data.SalaryType;

/**
 * Created by Alenka on 20.07.2016.
 */
public class Tester extends Employee {

    private static double salary = 20;
    private static SalaryType salaryType = SalaryType.HourlyPayment;

    public Tester() {
        super(salaryType, salary);
        setSetProfessions(EmployeeType.Tester);
    }

    public static double getSalary() {
        return salary;
    }

    public static SalaryType getSalaryType() {
        return salaryType;
    }

}
