package employee;

import data.EmployeeType;
import data.SalaryType;

/**
 * Created by Alenka on 20.07.2016.
 */
public class Designer extends Employee {

    private static double salary = 25;
    private static SalaryType salaryType = SalaryType.HourlyPayment;

    public Designer() {
        super(salaryType, salary);
        setSetProfessions(EmployeeType.Designer);
    }

    public static double getSalary() {
        return salary;
    }

    public static SalaryType getSalaryType() {
        return salaryType;
    }

}
