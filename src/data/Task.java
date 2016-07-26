package data;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by Alenka on 21.07.2016.
 */
public class Task {

    private static int taskCounter = 0;
    private int taskNumber;
    private TaskType taskType;
    private TaskStatus taskStatus;
    private double taskDuration;
    private TaskPriority taskPriority;
    private Calendar currentDate;
    private boolean weekendTask;
    private double salaryForTask;


    public Task(Calendar currentDate) {
        this.currentDate = (Calendar)currentDate.clone();
        taskNumber = ++taskCounter;
        taskPriority = generateTaskPriority();
        taskType = generateTaskType();
        taskDuration = new Random().nextDouble()+1;
//        taskDuration = new BigDecimal(new Random().nextDouble()+1).setScale(2, RoundingMode.UP).doubleValue();  // длительность задания в дробной записи в формате 1.23 отбрасывание дробной части меньше сотых
        taskStatus = TaskStatus.New;
        weekendTask = setWeekendTask(currentDate);
        salaryForTask = 0.0;
    }


    /**
     * Метод который указывает выходной или будний день недели в него передали
     * @param currentDate - день который деобходимо проверить
     * @return true если день недели суббота или воскресенье, false в дргуих случаях
     */
    private boolean setWeekendTask(Calendar currentDate) {
        return (currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
    }

    /**
     * Метод для генерации типа для задания из списка TaskType
     * @return тип для задания выбранный случайным способом
     */
    private TaskType generateTaskType() {
        return TaskType.values()[new Random().nextInt(6)+1];
    }

    /**
     * Метод для генерации приоритета для задания из списка TaskPriority
     * @return приоритет для задания выбранный случайным способом
     */
    private TaskPriority generateTaskPriority() {
        return TaskPriority.values()[new Random().nextInt(3)];
    }

    /**
     * Метод для перевода значения времени с дробного значения в строковый в формате времени
     * @param time дробное число, например 1.75 = 1 час 45 мин
     * @return строка в формате %dh:%02dm, готовая для вывода на экран
     */
    public static String convertDoubleToHoursMinute(double time) {
        int hours = (int) (time * 60) / 60;
        int minutes = (int) (time * 60) % 60;
        return String.format("%dh:%02dm", hours, minutes);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskNumber=" + taskNumber +
                ", taskType=" + taskType +
                ", taskStatus=" + taskStatus +
                ", taskDuration=" + convertDoubleToHoursMinute(taskDuration)+
                ", taskPriority=" + taskPriority +
                ", isWeekendTask=" + weekendTask +
                ", currentDate=" + currentDate.getTime() +
                ", salaryForTask=" + salaryForTask +
                '}';
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public double getTaskDuration() {
        return taskDuration;
    }

    public TaskPriority getTaskPriority() {
        return taskPriority;
    }

    public double getSalaryForTask() {
        return salaryForTask;
    }

    public void setSalaryForTask(double salaryForTask) {
        this.salaryForTask = salaryForTask;
    }

    public boolean isWeekendTask() {
        return weekendTask;
    }

    public Calendar getCurrentDate() {
        return currentDate;
    }

}
