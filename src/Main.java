import data.*;
import employee.*;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Alenka on 20.07.2016.
 */
public class Main {

    private static final int MAX_EMPLOYEE_QUANTITY = 100;
    private static final int MIN_EMPLOYEE_QUANTITY = 10;
    private final static Calendar BEGIN_MONTH = new GregorianCalendar(2016, 06, 01, 00, 00, 00); // месяц начался 01 июля 2016 00:00:00


    private static List<Employee> employeeList = new ArrayList<>();
    //    public static List<PositionsType> necessaryEmployee = Arrays.asList(new PositionsType[] {PositionsType.Director, PositionsType.Accountant, PositionsType.Manager});
    private static Map<Task, Employee> taskEmployeeMap = new HashMap<>();
    private static BufferedReader bufferedReader = null;
    private static BufferedWriter bufferedWriter = null;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private static SimpleDateFormat sdfYearMonthDay = new SimpleDateFormat("yyyy.MM.dd");
    private static SimpleDateFormat sdfMonthYear = new SimpleDateFormat("MM.yyyy");
    private static DecimalFormat df2 = new DecimalFormat("#.##");


    public static void main(String[] args) {

        /**
         * Считываем имя файла и его расположение для запоминания результатов работы программы
         */

        System.out.println("INPUT PATH TO FILE FOR OUTPUT: (if entered empty line use default = c:\\output.txt)");
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String outFileName = bufferedReader.readLine();
            bufferedWriter = new BufferedWriter(new FileWriter((outFileName.equals("")) ? "c:\\output.txt" : outFileName));

            Calendar currentDate = (Calendar) BEGIN_MONTH.clone();
            /**
             * Создаем список случайных сотрудников
             */
            createListEmployee();

            /**
             * Создаем заглушку-маркер для отображения задач которые отданы на фриланс
             */
            Employee freelancer = new Freelancer();

            while ((BEGIN_MONTH.get(Calendar.MONTH) + 1) > currentDate.get(Calendar.MONTH)) {
//                bufferedWriter.write("Current date and time: " + sdfYearMonthDay.format(currentDate.getTime()) + "\r\n");
                askDirectorForTask(currentDate);

                for (Map.Entry<Task, Employee> entry : taskEmployeeMap.entrySet()) {

                    /**
                     * Проверяем задания которые имеют статус InProcess на окончание времени для выполнения, и в случае если время на выполнение задание прошло
                     * - меняем статус задания на TaskStatus.Completed
                     * - меняем статус сотрудника на свободный для новой задачи
                     */
                    if (entry.getKey().getTaskStatus() == TaskStatus.InProcess) {
                        if ((currentDate.getTimeInMillis() - entry.getKey().getCurrentDate().getTimeInMillis()) > entry.getKey().getTaskDuration() * 60 * 60 * 1000) {
                            entry.getKey().setTaskStatus(TaskStatus.Completed);
                            entry.getValue().setStatusEmployeeFree(true);
                        }
                    }
                    /**
                     * Выбираем задания которые имеют статус New
                     */
                    if (entry.getKey().getTaskStatus() == TaskStatus.New) {
                        Employee selectedEmployee = selectEmployeeForTask(entry.getKey());
                        /**
                         * Если для текущего задания выбран свободный сотрудник с имеющимися навыками
                         * - вносим в карту taskEmployeeMap сотрудника выбраного в методе selectEmployeeForTask, теперь текущей задаче соответсвует один сотрудник
                         * - высчитываем и вносим в данные задания цену выполнения текущим сотрудником
                         * - изменяем статус работника на Занят выполнением задания
                         * - изменение статуса задания на TaskStatus.InProcess
                         */
                        if (selectedEmployee != null) {
                            entry.setValue(selectedEmployee); //
                            entry.getKey().setSalaryForTask(calculateSalary(entry.getKey(), entry.getValue()));
                            selectedEmployee.setStatusEmployeeFree(false); //
                            entry.getKey().setTaskStatus(TaskStatus.InProcess);
                        }
                        /**
                         * Если для текущего задания нет свободного сотрудника
                         * - вносим в карту taskEmployeeMap данные что данная задача передана на фриланс
                         * - высчитываем и вносим в данные задания цену выполнения фрилансером
                         * - изменение статуса задания на TaskStatus.InProcess
                         */
                        else {
                            if (!entry.getKey().getTaskType().equals(TaskType.OfficeCleaning)) {
                                entry.setValue(freelancer);
                                entry.getKey().setSalaryForTask(calculateSalary(entry.getKey(), freelancer));
                                entry.getKey().setTaskStatus(TaskStatus.InProcess);
                            }
                            /**
                             * Если для текущего задания нет свободного сотрудника и это задание уборка офиса
                             * - изменение статуса задания на TaskStatus.Completed
                             * - вносим "заглушку" сотрудника с квалификацией Уборщика
                             */
                            if (entry.getKey().getTaskType().equals(TaskType.OfficeCleaning)) {
                                entry.getKey().setTaskStatus(TaskStatus.Completed);
                                entry.setValue(new Cleaner());
                            }
                        }
                    }
                }
                /**
                 * Переходим на один час вперед
                 */
                currentDate.add(Calendar.HOUR_OF_DAY, 1);

                /**
                 * если текущее время 00:00:00 любого дня:
                 * - обновить дневной счетчик рабочего времени у всех рабочих, в случае если началась новая неделя - то обнулить недельный счетчик рабочего времени
                 * - расчет бухгалтером оплаты за день выполненения работы фрилансерами
                 */
                if (currentDate.get(Calendar.HOUR_OF_DAY) == 00) {
                    renewHoursToEmployee(currentDate); //
                    calculateFreelanceDaySalary((Calendar) currentDate.clone());

                    /**
                     * если текущий день недели совпадает с днем недели начала месяца то Бухгалтер начисляет зарплату сотрудникам исходя из фактически отработанных часов
                     */
                    if (currentDate.get(Calendar.DAY_OF_WEEK) == BEGIN_MONTH.get(Calendar.DAY_OF_WEEK)) {
                        calculateEmployeeWeekSalary((Calendar) currentDate.clone());
                    }
                    /**
                     * По окончании месяца формируется суммарный отчет о выполненной работе и выданной зарплате по всем рабочим(сотрудникам офиса и фрилансерам) и для каждого сотрудника в отдельности, и сохранить его в текстовый документ
                     */
                    if (currentDate.get(Calendar.MONTH) > (BEGIN_MONTH.get(Calendar.MONTH))) {
                        calculateEmployeeMonthSalary((Calendar) currentDate.clone());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Something going wrong. Contact coder :)");
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Метод для подсчета и формирования отчетности бухгалтером за месяц
     * @param currentDate - передается текущая дата отчета
     * в результате в файл записывается данные по всем сотрудникам, их заработная плата и количество часов потраченых на выполнение заданий
     */
    private static void calculateEmployeeMonthSalary(Calendar currentDate) throws IOException {
        currentDate.add(Calendar.HOUR_OF_DAY, -1);
        double[] sumTime = new double[employeeList.size()];
        double[] sumSalary = new double[employeeList.size()];
        double sumTimeFreelancer = 0.0d;
        double sumSalaryFreelancer = 0.0d;
        double allTimeTask = 0.0d;
        double allSalary = 0.0d;
        for (Map.Entry<Task, Employee> entry : taskEmployeeMap.entrySet()) {
            for (int i = 0; i < employeeList.size(); i++) {
                if (entry.getValue().equals(employeeList.get(i))) {
                    sumTime[i] += entry.getKey().getTaskDuration();
                    sumSalary[i] += entry.getKey().getSalaryForTask();
                }
                if (entry.getValue().getClass().equals(Freelancer.class)) {
                    sumTimeFreelancer += entry.getKey().getTaskDuration();
                    sumSalaryFreelancer += entry.getKey().getSalaryForTask();
                }
            }
        }

        for (int i = 0; i < employeeList.size(); i++) {
            for (int j = 0; j < EmployeeType.values().length; j++) {
                if (EmployeeType.values()[j].getEmployeeTypeName().equals(employeeList.get(i).getClass().getSimpleName())) {
                    if (EmployeeType.values()[j].getSalaryType() == SalaryType.FixedSalary) {
                        sumSalary[i] += EmployeeType.values()[j].getEmployeeSalary();
                    }
                }
            }
            allTimeTask += sumTime[i];
            allSalary += sumSalary[i];
        }

//        System.out.println("Salary month report " + sdfMonthYear.format(currentDate.getTime()));
        bufferedWriter.write("Salary month report " + sdfMonthYear.format(currentDate.getTime()) + "\r\n");
        for (int i = 0; i < employeeList.size(); i++) {
//            System.out.println(employeeList.get(i) + " has salary = " + df2.format(sumSalary[i]) + "$, work time = " + Task.convertDoubleToHoursMinute(sumTime[i]));
            bufferedWriter.write(employeeList.get(i) + " has salary = " + df2.format(sumSalary[i]) + "$, work time = " + Task.convertDoubleToHoursMinute(sumTime[i]) + "\r\n");
        }
//        System.out.println("Freelancer has salary = " + df2.format(sumSalaryFreelancer) + "$, work time = " + Task.convertDoubleToHoursMinute(sumTimeFreelancer));
        bufferedWriter.write("Freelancer has salary = " + df2.format(sumSalaryFreelancer) + "$, work time = " + Task.convertDoubleToHoursMinute(sumTimeFreelancer) + "\r\n");

//        System.out.println("All salary = " + df2.format(allSalary+sumSalaryFreelancer) + "$, all work time = " + Task.convertDoubleToHoursMinute(allTimeTask+sumTimeFreelancer));
        bufferedWriter.write("All salary = " + df2.format(allSalary+sumSalaryFreelancer) + "$, all work time = " + Task.convertDoubleToHoursMinute(allTimeTask+sumTimeFreelancer) + "\r\n");
    }

    /**
     * Метод для подсчета и формирования отчетности бухгалтером за неделю
     * @param currentDate - передается текущая дата отчета
     */
    private static void calculateEmployeeWeekSalary(Calendar currentDate) throws IOException {
        currentDate.add(Calendar.HOUR_OF_DAY, -1);
        double[] sumTime = new double[employeeList.size()];
        double[] sumSalary = new double[employeeList.size()];
        for (Map.Entry<Task, Employee> entry : taskEmployeeMap.entrySet()) {
            if (currentDate.get(Calendar.DAY_OF_YEAR) - entry.getKey().getCurrentDate().get(Calendar.DAY_OF_YEAR) < 7) {
                for (int i = 0; i < employeeList.size(); i++) {
                    if (entry.getValue().equals(employeeList.get(i))) {
                        sumTime[i] += entry.getKey().getTaskDuration();
                        sumSalary[i] += entry.getKey().getSalaryForTask();
                    }
                }
            }
        }
//        bufferedWriter.write("Salary reporting in week end by date - " + sdfYearMonthDay.format(currentDate.getTime()) + "\r\n");
//        for (int i = 0; i < employeeList.size(); i++) {
//            bufferedWriter.write("Salary " + employeeList.get(i) + ", equals " + df2.format(sumSalary[i]) + ", work time = " + Task.convertDoubleToHoursMinute(sumTime[i]) + "\r\n");
//        }
    }

    /**
     * Метод для подсчета и формирования отчетности бухгалтером для фрилансеров за день
     * @param currentDate - передается текущая дата отчета
     */
    private static void calculateFreelanceDaySalary(Calendar currentDate) throws IOException {
        currentDate.add(Calendar.HOUR_OF_DAY, -1);
        double sumTime = 0.0d;
        double sumSalary = 0.0d;
        for (Map.Entry<Task, Employee> entry : taskEmployeeMap.entrySet()) {
            if (entry.getValue().getClass().equals(Freelancer.class)) {
                if (entry.getKey().getCurrentDate().get(Calendar.DAY_OF_YEAR) == currentDate.get(Calendar.DAY_OF_YEAR)) {
                    sumTime += entry.getKey().getTaskDuration();
                    sumSalary += entry.getKey().getSalaryForTask();
                }
            }
        }
//        bufferedWriter.write(sdfYearMonthDay.format(currentDate.getTime()) + " salary freelancers equals " + df2.format(sumSalary) + ", work time = " + Task.convertDoubleToHoursMinute(sumTime) + "\r\n");
    }
    /**
     * Метод для обновления дневного счетчика рабочего времени у всех рабочих
     * так же, если текущий день недели совпадает с днем недели начала месяца то обновляется и недельный счетчик рабочего времени
     * @param currentDate - передается текущая дата отчета
     */
    private static void renewHoursToEmployee(Calendar currentDate) {

        for (int i = 0; i < employeeList.size(); i++) {
            employeeList.get(i).setTimeLeftWorkDay(Employee.MAX_PER_DAY_HOURS);
        }
        if (currentDate.get(Calendar.DAY_OF_WEEK) == BEGIN_MONTH.get(Calendar.DAY_OF_WEEK)) {
            for (Employee employee : employeeList) {
                employee.setTimeLeftWorkWeek(Employee.MAX_PER_WEEK_HOURS);
            }
        }
    }

    /**
     * Метод для опроса всех сотрудников с навыком Директор для получения новых задач
     * каждая выдача новых задач уменьшает на 1 час счетчик оставшегося времени у сотрудника
     * новые задачи заносятся в карту заданий и исполнителей, в качестве исполнителя вносится null, чтобы указать что данная задача не была передана сотрудникам или фрилансерам
     * @param currentDate - передается текущая дата отчета
     */
    private static void askDirectorForTask(Calendar currentDate) {
        List<Task> tempTasks;
        for (int i = 0; i < employeeList.size(); i++) {
            if ((employeeList.get(i).getSetProfessions().contains(EmployeeType.Director)) && (employeeList.get(i).getTimeLeftWorkDay() > 1)) {
                employeeList.get(i).setTimeLeftWorkDay(employeeList.get(i).getTimeLeftWorkDay() - 1); // выдача задания отнимает у Директора 1 час его времени
                tempTasks = Director.generateTasksEveryHour(currentDate);
                for (int j = 0; j < tempTasks.size(); j++)
                    taskEmployeeMap.put(tempTasks.get(j), null);
            }
        }
    }

    /**
     * Метод для расчета затрат на выполнения задания
     * - расчитвается для почасовой работы сотрудниками и фрилансерами
     * - в случае работы в выходные стоимость выполнения задания увеличивается в 2 раза
     * @param task - передается задача
     * @param employee - передается сотрудник, который будет выполнять задачу
    */
    private static double calculateSalary(Task task, Employee employee) {
        double salary = 0.0d;
        EmployeeType employeeType = task.getTaskType().getEmployeeType();
        if ((employeeType.getSalaryType().equals(SalaryType.HourlyPayment))) {
            salary = task.getTaskDuration() * employeeType.getEmployeeSalary();
        }
        if ((employee.getClass().equals(Freelancer.class))){
            salary = task.getTaskDuration() * EmployeeType.valueOf(employee.getClass().getSimpleName()).getEmployeeSalary();
        }
        return salary * (task.isWeekendTask() ? 2 : 1);
    }

    /**
     * Метод для выбора для задания сотрудника
     * - выбирается сотрудник, который не занят выполнением других заданий
     * - у которого есть специальность необходимая для выполнения задания
     * - у которого счетчики оставшегося времени позволяют выполнить текущее задание
     * Если все условия соблюдены - то у текущего сотрудника уменьшаются счетчики оставшегося времени на день и месяц
     * * @param task - передается задача для которой необходимо выбрать сотрудника, или вернуть null если такого не обнаружено
     */
    private static Employee selectEmployeeForTask(Task task) {

        EmployeeType employeeType = task.getTaskType().getEmployeeType();
        Employee selectedEmployee = null;

        for (int i = 0; i < employeeList.size(); i++) {
            if ((employeeList.get(i).isStatusEmployeeFree()) && (employeeList.get(i).getSetProfessions().contains(employeeType))) {
                if ((employeeList.get(i).getTimeLeftWorkDay() > task.getTaskDuration()) && (employeeList.get(i).getTimeLeftWorkWeek() > task.getTaskDuration())) {
                    selectedEmployee = employeeList.get(i);
                    selectedEmployee.setTimeLeftWorkDay(selectedEmployee.getTimeLeftWorkDay() - task.getTaskDuration()); //
                    selectedEmployee.setTimeLeftWorkWeek(selectedEmployee.getTimeLeftWorkWeek() - task.getTaskDuration()); //
                    break;
                }
            }
        }
        return selectedEmployee;
    }
    /**
     * Метод генерации списка сотрудников с возможными должностями и специализациями
     * в фирме должны быть хотя бы один Директор, Менеджер и Бухгалтер
     */
    private static void createListEmployee() throws IOException {
        int amountEmployee = new Random().nextInt(MAX_EMPLOYEE_QUANTITY - MIN_EMPLOYEE_QUANTITY - 3) + MIN_EMPLOYEE_QUANTITY;
        employeeList.add(new Director()); // necessaryEmployee
        employeeList.add(new Accountant()); // necessaryEmployee
        employeeList.add(new Manager()); // necessaryEmployee

        while (amountEmployee > 0) {
            switch (new Random().nextInt(7)) {
                case 0:
                    employeeList.add(new Director());
                    break;
                case 1:
                    employeeList.add(new Accountant());
                    break;
                case 2:
                    employeeList.add(new Manager());
                    break;
                case 3:
                    employeeList.add(new Cleaner());
                    break;
                case 4:
                    employeeList.add(new Programmer());
                    break;
                case 5:
                    employeeList.add(new Designer());
                    break;
                case 6:
                    employeeList.add(new Tester());
                    break;
            }
            amountEmployee--;
        }

//        bufferedWriter.write("List of Employee in office (" + employeeList.size() + "pcs.): " + "\r\n");
//        for (int i = 0; i < employeeList.size(); i++) {
//            bufferedWriter.write(employeeList.get(i) + " with list positions: " + employeeList.get(i).getSetProfessions().toString() + "\r\n");
//        }
    }
}
