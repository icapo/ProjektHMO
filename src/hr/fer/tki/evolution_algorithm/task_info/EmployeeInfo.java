package hr.fer.tki.evolution_algorithm.task_info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class EmployeeInfo implements Cloneable {

	private String employeeID;
	private int employeeIndex;
	private HashMap<Shift, Integer> maxShifts;
	private List<Integer> daysOff;
	private int maxTotalMinutes;
	private int minTotalMinutes;
	private int maxConsecutiveShifts;
	private int minConsecutiveShifts;
	private int minConsecutiveDaysOff;
	private int maxWeekends;
	private List<ShiftRequest> shiftOnRequests;
	private List<ShiftRequest> shiftOffRequests;

	public EmployeeInfo(String ID, int fileIndex, int maxTotalMinutes, int minTotalMinutes,
			int maxConsecutiveShifts, int minConsecutiveShifts,
			int minConsecutiveDaysOff, int maxWeekends) {
		super();
		this.employeeID = ID;
		this.employeeIndex = fileIndex;
		this.maxTotalMinutes = maxTotalMinutes;
		this.minTotalMinutes = minTotalMinutes;
		this.maxConsecutiveShifts = maxConsecutiveShifts;
		this.minConsecutiveShifts = minConsecutiveShifts;
		this.minConsecutiveDaysOff = minConsecutiveDaysOff;
		this.maxWeekends = maxWeekends;
		this.maxShifts = new HashMap<Shift, Integer>();
		this.daysOff = new ArrayList<Integer>();
		this.shiftOffRequests = new ArrayList<ShiftRequest>();
		this.shiftOnRequests = new ArrayList<ShiftRequest>();
	}
	
	public int getEmployeeIndex() {
		return this.employeeIndex;
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String iD) {
		employeeID = iD;
	}

	public HashMap<Shift, Integer> getMaxShifts() {
		return maxShifts;
	}

	public void addDayOff(int dayIndex) {
		this.daysOff.add(dayIndex);
	}

	public void addShiftOnRequest(ShiftRequest shift) {
		this.shiftOnRequests.add(shift);
	}

	public void setShiftOnRequest(List<ShiftRequest> list) {
		this.shiftOnRequests = list;
	}

	public void setShiftOffRequest(List<ShiftRequest> list) {
		this.shiftOffRequests = list;
	}

	public void setDaysOff(List<Integer> list) {
		this.daysOff = list;
	}
	
	public void addShiftOffRequest(ShiftRequest shift) {
		this.shiftOffRequests.add(shift);
	}
	
	public List<Integer> getDaysOff() {
		return daysOff;
	}

	public int getMaxShifts(Shift shift) {
		return this.maxShifts.get(shift);
	}

	public void setMaxShifts(HashMap<Shift, Integer> maxShifts) {
		this.maxShifts = maxShifts;
	}

	public void setMaxShift(Shift shift, Integer numberOfShifts) {
		this.maxShifts.put(shift, numberOfShifts);
	}

	public void putMaxShift(Shift shift, int max) {
		this.maxShifts.put(shift, max);
	}

	public int getMaxTotalMinutes() {
		return maxTotalMinutes;
	}

	public void setMaxTotalMinutes(int maxTotalMinutes) {
		this.maxTotalMinutes = maxTotalMinutes;
	}

	public int getMinTotalMinutes() {
		return minTotalMinutes;
	}

	public void setMinTotalMinutes(int minTotalMinutes) {
		this.minTotalMinutes = minTotalMinutes;
	}

	public int getMaxConsecutiveShifts() {
		return maxConsecutiveShifts;
	}

	public void setMaxConsecutiveShifts(int maxConsecutiveShifts) {
		this.maxConsecutiveShifts = maxConsecutiveShifts;
	}

	public int getMinConsecutiveShifts() {
		return minConsecutiveShifts;
	}

	public void setMinConsecutiveShifts(int minConsecutiveShifts) {
		this.minConsecutiveShifts = minConsecutiveShifts;
	}

	public int getMinConsecutiveDaysOff() {
		return minConsecutiveDaysOff;
	}

	public void setMinConsecutiveDaysOff(int minConsecutiveDaysOff) {
		this.minConsecutiveDaysOff = minConsecutiveDaysOff;
	}

	public int getMaxWeekends() {
		return maxWeekends;
	}

	public void setMaxWeekends(int maxWeekends) {
		this.maxWeekends = maxWeekends;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((employeeID == null) ? 0 : employeeID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeInfo other = (EmployeeInfo) obj;
		if (employeeID == null) {
			if (other.employeeID != null)
				return false;
		} else if (!employeeID.equals(other.employeeID))
			return false;
		return true;
	}

	@Override
	public EmployeeInfo clone() {
		EmployeeInfo employee = new EmployeeInfo(this.employeeID, this.employeeIndex, this.maxTotalMinutes, this.minTotalMinutes, this.maxConsecutiveShifts, this.minConsecutiveShifts, this.minConsecutiveDaysOff, this.maxWeekends);
		employee.setMaxShifts((HashMap<Shift, Integer>) this.maxShifts.clone());
		employee.setShiftOnRequest((List<ShiftRequest>) ((ArrayList)this.shiftOnRequests).clone());
		employee.setShiftOffRequest((List<ShiftRequest>) ((ArrayList)this.shiftOffRequests).clone());
		employee.setDaysOff((List<Integer>) ((ArrayList)this.daysOff).clone());
		return employee;
	}

}
