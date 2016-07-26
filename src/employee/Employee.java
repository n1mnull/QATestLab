package employee;

import data.EmployeeType;
import data.SalaryType;

import java.util.*;

/**
 * Created by Alenka on 20.07.2016.
 */
public class Employee {

    public static final double MAX_PER_DAY_HOURS = 8;
    public static final double MAX_PER_WEEK_HOURS = 40;

    private static int countEmployee;
    private String employeeName;
    private SalaryType salaryType;
    private double salary;
    private boolean statusEmployeeFree; // true = free to work, false = busy
    private double timeLeftWorkDay;
    private double timeLeftWorkWeek;

    private Set<EmployeeType> setProfessions = new HashSet<>();

    public Employee(SalaryType salaryType, double salary) {
        countEmployee++;
        this.employeeName = "EmployeeName-" + countEmployee;
        this.salaryType = salaryType;
        this.salary = salary;
        this.statusEmployeeFree = true;
        this.timeLeftWorkDay = MAX_PER_DAY_HOURS;
        this.timeLeftWorkWeek = MAX_PER_WEEK_HOURS;

    }

    public boolean isStatusEmployeeFree() {
        return statusEmployeeFree;
    }

    public void setStatusEmployeeFree(boolean statusEmployeeFree) {
        this.statusEmployeeFree = statusEmployeeFree;
    }

    public Set<EmployeeType> getSetProfessions() {
        return setProfessions;
    }

    public void setSetProfessions(EmployeeType employeeType) {
        setProfessions.add(employeeType);
        while (true) {
            int bonusProfession = new Random().nextInt(7);
            if ( (getSetProfessions().contains(EmployeeType.Director) || (getSetProfessions().contains(EmployeeType.Accountant))) && bonusProfession != 2) { // если должность Director или Accountant, то возможно получить должность только Manager
                break;
            }
            if (( getSetProfessions().contains(EmployeeType.Director) || getSetProfessions().contains(EmployeeType.Accountant)) && (getSetProfessions().contains(EmployeeType.Manager))) {
                break;
            }
            if ((getSetProfessions().contains(EmployeeType.Manager) && getSetProfessions().size() > 1) && bonusProfession < 2) {
                break;
            }
            if ((getSetProfessions().size() > 0) && !(getSetProfessions().contains(EmployeeType.Manager)) && bonusProfession < 2) {
                break;
            }
            if (bonusProfession == 3 || employeeType.ordinal() == 3) { // попытка дать специальность уборщика любому сотруднику с должностью или дать новую специальность уборщику
                break;
            }
//            if (bonusProfession == employeeType.ordinal()) { // попытка дать специальность которая уже есть (эта проверка нужнка при использовании List для хранения должностей в setProfessions)
//                break;
//            }
            setProfessions.add(EmployeeType.values()[bonusProfession]);
        }
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public double getTimeLeftWorkDay() {
        return timeLeftWorkDay;
    }

    public double getTimeLeftWorkWeek() {
        return timeLeftWorkWeek;
    }

    public void setTimeLeftWorkDay(double timeLeftWorkDay) {
        this.timeLeftWorkDay = timeLeftWorkDay;
    }

    public void setTimeLeftWorkWeek(double timeLeftWorkWeek) {
        this.timeLeftWorkWeek = timeLeftWorkWeek;
    }

    @Override
    public String toString() {
        return /*"Employee{name=" +*/
                employeeName + ", type=" +
                this.getClass().getSimpleName()+
                "";
    }
}
