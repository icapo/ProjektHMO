package hr.fer.tki.evolution_algorithm.task_info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskInfo {
    private int numberOfDays;
    /*
     * Shifts by shift ID
     */
    private HashMap<String, Shift> shifts;
    /*
     * Employees by employee ID
     */
    private HashMap<String, EmployeeInfo> staff;
    private List<ShiftRequest> shiftOnRequests;
    private List<ShiftRequest> shiftOffRequest;
    private Map<Integer,List<Cover>> cover;

    public TaskInfo() {
        super();
        this.shifts = new HashMap<String, Shift>();
        this.staff = new HashMap<String, EmployeeInfo>();
        this.shiftOnRequests = new ArrayList<ShiftRequest>();
        this.shiftOffRequest = new ArrayList<ShiftRequest>();
        this.cover = new HashMap<Integer,List<Cover>>();
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public HashMap<String, Shift> getShifts() {
        return shifts;
    }

    public void setShifts(HashMap<String, Shift> shifts) {
        this.shifts = shifts;
    }

    public HashMap<String, EmployeeInfo> getStaff() {
        return staff;
    }

    public Shift getShift(String shiftID) {
        return this.shifts.get(shiftID);
    }

    public EmployeeInfo getEmployee(String employeeID) {
        return this.staff.get(employeeID);
    }

    public void setStaff(HashMap<String, EmployeeInfo> staff) {
        this.staff = staff;
    }

    public List<ShiftRequest> getShiftOnRequests() {
        return shiftOnRequests;
    }

    public void setShiftOnRequests(List<ShiftRequest> shiftOnRequests) {
        this.shiftOnRequests = shiftOnRequests;
    }

    public List<ShiftRequest> getSiftOffrequest() {
        return shiftOffRequest;
    }

    public void setSiftOffrequest(List<ShiftRequest> siftOffrequest) {
        this.shiftOffRequest = siftOffrequest;
    }

    public Map<Integer, List<Cover>> getCover() {
        return cover;
    }

    public void setCover(List<Cover> cover) {
        this.cover.clear();
        for (Cover cover1 : cover) {
            List<Cover> covers = this.cover.get(cover1.getDay());
            if (covers == null) {
                covers = new ArrayList<>();
                covers.add(cover1);
                this.cover.put(cover1.getDay(), covers);
            } else {
                covers.add(cover1);
            }
        }
    }


    public void addShift(String shiftID, Shift shift) {

        this.shifts.put(shiftID, shift);
    }

    public void addStaff(String employeeID, EmployeeInfo employee) {
        this.staff.put(employeeID, employee);
    }

    public EmployeeInfo getStaff(String employeeID) {
        return this.staff.get(employeeID);
    }

    public void addCover(Cover cover) {
        List<Cover> covers = this.cover.get(cover.getDay());
        if (covers == null) {
            covers = new ArrayList<>();
            covers.add(cover);
            this.cover.put(cover.getDay(), covers);
        } else {
            covers.add(cover);
        }
    }
}
