package hr.fer.zemris.evolution_algorithm.chromosome;


public interface IChromosome {
	
	public String[][] getElements();

	public void setElements(String[][] elements);
	
	Object getChromosomeElement(int row, int col);
	
	void setChromosomeElement(int row, int col, String value);
	
	IChromosome copy();
	
	double getFitness();
	
	void setFitness(double value);
	
}
