package employee;

import data.EmployeeType;
import data.SalaryType;
import data.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by Alenka on 20.07.2016.
 */
public class Director extends Employee {

    public final static int MAX_TASK_QUANTITY = 5; // условно взял что Директор дает до 5 заданий за один час, но как минимум одно!
    public final static int MIN_TASK_QUANTITY = 1; // условно взял что Директор дает до 5 заданий за один час, но как минимум одно!

    private static double salary = 10000;
    private static SalaryType salaryType = SalaryType.FixedSalary;
    private int countTaskGeneration = (int) Employee.MAX_PER_DAY_HOURS;


    public Director() {
        super(salaryType, salary);
        setSetProfessions(EmployeeType.Director);
    }

    public static List<Task> generateTasksEveryHour(Calendar currentDate) {
        List<Task> newTasks = new ArrayList<>();
        int taskQuantity = new Random().nextInt(MAX_TASK_QUANTITY-MIN_TASK_QUANTITY) + MIN_TASK_QUANTITY;
        for (int i = 0; i < taskQuantity; i++) {
            newTasks.add(new Task(currentDate));
        }
        return newTasks;
    }

    public int getCountTaskGeneration() {
        return countTaskGeneration;
    }

    public void setCountTaskGeneration(int countTaskGeneration) {
        this.countTaskGeneration = countTaskGeneration;
    }

    public static double getSalary() {
        return salary;
    }

    public static SalaryType getSalaryType() {
        return salaryType;
    }
}
