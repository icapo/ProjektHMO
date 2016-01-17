package hr.fer.zemris.evolution_algorithm.task_info;

import java.util.ArrayList;
import java.util.List;

public class Shift implements Cloneable{
	
	private String shiftID;
	private int lengthInMins;
	private List<Shift> cannotFollow;
	
	
	public Shift(String shiftID, int lengthInMins) {
		super();
		this.shiftID = shiftID;
		this.lengthInMins = lengthInMins;
		this.cannotFollow = new ArrayList<Shift>();
	}
	
	public Shift(String shiftID, int lengthInMins, List<Shift> cannotFollow) {
		super();
		this.shiftID = shiftID;
		this.lengthInMins = lengthInMins;
		this.cannotFollow = cannotFollow;
	}


	public String getShiftID() {
		return shiftID;
	}
	
	public boolean checkIfCanFollow(Shift shift) {
		return !this.cannotFollow.contains(shift);
	}


	public void setShiftID(String shiftID) {
		this.shiftID = shiftID;
	}


	public int getLengthInMins() {
		return lengthInMins;
	}


	public void setLengthInMins(int lengthInMins) {
		this.lengthInMins = lengthInMins;
	}


	public List<Shift> getCannotFollow() {
		return cannotFollow;
	}


	public void setCannotFollow(List<Shift> cannotFollow) {
		this.cannotFollow = cannotFollow;
	}
	
	public void addCannotFollow(Shift shift) {
		this.cannotFollow.add(shift);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((shiftID == null) ? 0 : shiftID.hashCode());
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
		Shift other = (Shift) obj;
		if (shiftID == null) {
			if (other.shiftID != null)
				return false;
		} else if (!shiftID.equals(other.shiftID))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.shiftID;
	}
	
	@Override
	public Shift clone() {
		Shift shift = new Shift(this.shiftID, this.lengthInMins, this.cannotFollow);
		return shift;
	}

}
