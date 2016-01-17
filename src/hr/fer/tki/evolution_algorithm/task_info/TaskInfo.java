package hr.fer.tki.evolution_algorithm.task_info;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	private List<Cover> cover; 
	
	public TaskInfo() {
		super();
		this.shifts = new HashMap<String, Shift>();
		this.staff = new HashMap<String, EmployeeInfo>();
		this.shiftOnRequests = new ArrayList<ShiftRequest>();
		this.shiftOffRequest = new ArrayList<ShiftRequest>();
		this.cover = new ArrayList<Cover>();
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

	public List<Cover> getCover() {
		return cover;
	}

	public void setCover(List<Cover> cover) {
		this.cover = cover;
	}

	public void parse(String filepath) {
		// Open the file
		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String line;

		//Read File Line By Line
		try {
			while ((line = br.readLine()) != null)   {
				if (line.startsWith("#") || line.length() == 0) {
					continue;
				}
				
				if (line.startsWith("SECTION_HORIZON")) {
					while ((line = br.readLine()) != null)   {
						if (line.startsWith("#")) {
							continue;
						}
						if (line.length() == 0) {
							break;
						}
						
						this.numberOfDays = Integer.parseInt(line);
					}
				}
				
				if (line.startsWith("SECTION_SHIFTS")) {
					while ((line = br.readLine()) != null)   {
						if (line.startsWith("#")) {
							continue;
						}
						if (line.length() == 0) {
							break;
						}
						
						String[] elems =  line.split(",");
						String shiftID = elems[0];
						int lengthInMinutes = Integer.parseInt(elems[1]);
						
						this.shifts.put(shiftID, new Shift(shiftID, lengthInMinutes));
						
						if (elems.length == 3 && elems[2].contains("|")) {
							elems = elems[2].split("\\|");
							for (int i = 0; i < elems.length; i++) {
								this.shifts.get(shiftID).addCannotFollow(this.shifts.get(elems[i]));
							}
						}
					}
				}
				
				if (line.startsWith("SECTION_STAFF")) {
					int employeeIndex = 0;
					while ((line = br.readLine()) != null)   {
						if (line.startsWith("#")) {
							employeeIndex = 0;
							continue;
						}
						if (line.length() == 0) {
							break;
						}
						
						String[] elems =  line.split(",");
						String employeeID = elems[0];
						int maxTotalMins = Integer.parseInt(elems[2]);
						int minTotalMins = Integer.parseInt(elems[3]);
						int maxConsShifts = Integer.parseInt(elems[4]);
						int minConsShifts = Integer.parseInt(elems[5]);
						int minConsDaysOff = Integer.parseInt(elems[6]);
						int maxWeekends = Integer.parseInt(elems[7]);
						
						EmployeeInfo employee = new EmployeeInfo(employeeID, employeeIndex, maxTotalMins, minTotalMins, maxConsShifts, minConsShifts, minConsDaysOff, maxWeekends);
	
						if (elems[1].contains("|")) {
							elems = elems[1].split("\\|");
							for (int i = 0; i < elems.length; i++) {
								String[] equals = elems[i].split("=");
								employee.putMaxShift(this.shifts.get(equals[0]), Integer.parseInt(equals[1]));
							}
						}
						this.staff.put(employeeID, employee);
						employeeIndex++;
					}
				}
				
				if (line.startsWith("SECTION_DAYS_OFF")) {
					while ((line = br.readLine()) != null)   {
						if (line.startsWith("#")) {
							continue;
						}
						if (line.length() == 0) {
							break;
						}
						
						String[] elems =  line.split(",");
						String employeeID = elems[0];
						
						for (int i = 1; i < elems.length; i++) {
							this.staff.get(employeeID).addDayOff(Integer.parseInt(elems[i]));
						}
					}
				}
				
				if (line.startsWith("SECTION_SHIFT_ON_REQUESTS")) {
					while ((line = br.readLine()) != null)   {
						if (line.startsWith("#")) {
							continue;
						}
						if (line.length() == 0) {
							break;
						}
						
						String[] elems =  line.split(",");
						String employeeID = elems[0];
						int dayIndex = Integer.parseInt(elems[1]);
						String shiftID = elems[2];
						int weight = Integer.parseInt(elems[3]);
						
						ShiftRequest shiftReq = new ShiftRequest(dayIndex, shiftID, weight);
						
						this.staff.get(employeeID).addShiftOnRequest(shiftReq);
					}
				}
				
				if (line.startsWith("SECTION_SHIFT_OFF_REQUESTS")) {
					while ((line = br.readLine()) != null)   {
						if (line.startsWith("#")) {
							continue;
						}
						if (line.length() == 0) {
							break;
						}
						
						String[] elems =  line.split(",");
						String employeeID = elems[0];
						int dayIndex = Integer.parseInt(elems[1]);
						String shiftID = elems[2];
						int weight = Integer.parseInt(elems[3]);
						
						ShiftRequest shiftReq = new ShiftRequest(dayIndex, shiftID, weight);
						
						this.staff.get(employeeID).addShiftOffRequest(shiftReq);
					}
				}
				
				if (line.startsWith("SECTION_COVER")) {
					while ((line = br.readLine()) != null)   {
						if (line.startsWith("#")) {
							continue;
						}
						if (line.length() == 0) {
							break;
						}
						
						String[] elems =  line.split(",");
						int dayIndex = Integer.parseInt(elems[0]);
						String shiftID = elems[1];
						int requirement = Integer.parseInt(elems[2]);
						int weightUnder = Integer.parseInt(elems[3]);
						int weightOver = Integer.parseInt(elems[4]);
						
						this.cover.add(new Cover(dayIndex, shiftID, requirement, weightUnder, weightOver));
					}
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
