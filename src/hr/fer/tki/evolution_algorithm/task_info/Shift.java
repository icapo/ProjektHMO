package hr.fer.tki.evolution_algorithm.task_info;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shift implements Cloneable{

	private int id;
	private String shiftID;
	private int minutesLength;
	private Map<String, Shift> cannotFollow;
	
	
	public Shift(String shiftID, int minutesLength) {
		super();
		this.shiftID = shiftID;
		this.minutesLength = minutesLength;
		this.cannotFollow = new HashMap<String, Shift>();
	}
	
	public Shift(String shiftID, int minutesLength, Map<String, Shift> cannotFollow) {
		super();
		this.shiftID = shiftID;
		this.minutesLength = minutesLength;
		this.cannotFollow = cannotFollow;
	}


	public String getShiftID() {
		return shiftID;
	}
	
	public boolean checkIfCanFollow(Shift shift) {
		return !this.cannotFollow.containsKey(shift.getShiftID());
	}


	public void setShiftID(String shiftID) {
		this.shiftID = shiftID;
	}


	public int getLengthInMinutes() {
		return minutesLength;
	}


	public void setLengthInMins(int lengthInMins) {
		this.minutesLength = lengthInMins;
	}


	public Map<String, Shift> getCannotFollow() {
		return this.cannotFollow;
	}


	public void setCannotFollow(Map<String, Shift> cannotFollow) {
		this.cannotFollow = cannotFollow;
	}
	
	public void addCannotFollow(Shift shift) {
		this.cannotFollow.put(shift.getShiftID(),shift);
	}


	@Override
	public int hashCode() {
		return this.shiftID.hashCode();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Shift other = (Shift) obj;
		if (shiftID == null || other.shiftID == null) {
			return false;
		}
		return this.shiftID.equals(other.shiftID);
	}
	
	@Override
	public String toString() {
		return this.shiftID;
	}
	
	@Override
	public Shift clone() {
		Shift shift = new Shift(this.shiftID, this.minutesLength, this.cannotFollow);
		return shift;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
