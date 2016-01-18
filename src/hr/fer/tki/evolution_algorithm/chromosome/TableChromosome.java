package hr.fer.tki.evolution_algorithm.chromosome;


public class TableChromosome implements IChromosome {
	
	private String[][] data;
	private double fitness;
	
	public TableChromosome(int rows, int cols) {
		this( new String[rows][cols]);
	}
	
	public TableChromosome(String[][] data) {
		this.fitness = Double.MAX_VALUE;
		this.data = data;
	}

	public Object getChromosomeElement(int row, int col) {
		return this.data[row][col];
	}

	public void setChromosomeElement(int row, int col, String value) {
		this.data[row][col] = value;
	}

	public IChromosome copy() {
		return new TableChromosome(this.data.clone());
	}

	public double getFitness() {
		return this.fitness;
	}

	public void setFitness(double value) {
		this.fitness = value;
	}

	public int getRowsNum() {
		return this.data.length;
	}

	public int getColsNum() {
		return this.data[0].length;
	}
}
