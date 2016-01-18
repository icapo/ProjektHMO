package hr.fer.tki.evolution_algorithm.chromosome;


public interface IChromosome {
	
	Object getChromosomeElement(int row, int col);
	
	void setChromosomeElement(int row, int col, String value);
	
	public int getRowsNum();
	
	public int getColsNum();
	
	IChromosome copy();
	
	double getFitness();
	
	void setFitness(double value);
	
}
