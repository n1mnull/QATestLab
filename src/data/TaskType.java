package data;

/**
 * Created by Alenka on 21.07.2016.
 */
public enum TaskType {

    TasksGenerate(EmployeeType.Director),
    DataReporting(EmployeeType.Accountant),
    SaleOffer(EmployeeType.Manager),
    OfficeCleaning(EmployeeType.Cleaner),
    ProgramCoding(EmployeeType.Programmer),
    DesignMaking(EmployeeType.Designer),
    ProgramTesting(EmployeeType.Tester);

    EmployeeType employeeType;

    TaskType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }


}
