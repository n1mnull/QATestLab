package employee;

import data.SalaryType;

/**
 * Created by Alenka on 23.07.2016.
 */
public class Freelancer extends Employee {

    private static double salary = 20;
    private static SalaryType salaryType = SalaryType.HourlyPayment;

    public Freelancer() {
        super(salaryType, salary);
    }

    public static double getSalary() {
        return salary;
    }

    public static SalaryType getSalaryType() {
        return salaryType;
    }
}
