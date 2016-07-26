package employee;

import data.EmployeeType;
import data.SalaryType;

/**
 * Created by Alenka on 20.07.2016.
 */
public class Cleaner extends Employee {

    private static double salary = 10;
    private static SalaryType salaryType = SalaryType.HourlyPayment;


    public Cleaner() {
        super(salaryType, salary);
        setSetProfessions(EmployeeType.Cleaner);
    }

    public static double getSalary() {
        return salary;
    }

    public static SalaryType getSalaryType() {
        return salaryType;
    }

}
