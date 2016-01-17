package hr.fer.tki.evolution_algorithm.task_info;

public class Cover {

	private int day;
	private String shiftID;
	private int requirement;
	private int weightForUnder;
	private int weightForOver;
	
	public Cover(int day, String shiftID, int requirement, int weightForUnder,
			int weightForOver) {
		super();
		this.day = day;
		this.shiftID = shiftID;
		this.requirement = requirement;
		this.weightForUnder = weightForUnder;
		this.weightForOver = weightForOver;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getShiftID() {
		return shiftID;
	}

	public void setShiftID(String shiftID) {
		this.shiftID = shiftID;
	}

	public int getRequirement() {
		return requirement;
	}

	public void setRequirement(int requirement) {
		this.requirement = requirement;
	}

	public int getWeightForUnder() {
		return weightForUnder;
	}

	public void setWeightForUnder(int weightForUnder) {
		this.weightForUnder = weightForUnder;
	}

	public int getWeightForOver() {
		return weightForOver;
	}

	public void setWeightForOver(int weightForOver) {
		this.weightForOver = weightForOver;
	}
}
