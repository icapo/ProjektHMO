package hr.fer.tki.evolution_algorithm.task_info;

public class ShiftRequest implements Cloneable {

	private int dayIndex;
	private String shiftID;
	private int weight;
	
	public ShiftRequest(int dayIndex, String shiftID,
			int weight) {
		super();
		this.dayIndex = dayIndex;
		this.shiftID = shiftID;
		this.weight = weight;
	}

	public int getDayIndex() {
		return dayIndex;
	}

	public void setDayIndex(int dayIndex) {
		this.dayIndex = dayIndex;
	}

	public String getShiftID() {
		return shiftID;
	}

	public void setShiftID(String shiftID) {
		this.shiftID = shiftID;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	@Override
	public ShiftRequest clone() {
		return new ShiftRequest(this.dayIndex, this.shiftID, this.weight);
	}
}
