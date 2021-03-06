package hr.fer.tki.evolution_algorithm.chromosome;


public interface IChromosome {

    Object getChromosomeElement(int row, int col);

    void setChromosomeElement(int row, int col, String value);

    Object getChromosomeElement(int row, int col, boolean v);

    void setChromosomeElement(int row, int col, String value, boolean v );

    public int getRowsNum();

    public int getColsNum();

    IChromosome copy();

    double getFitness();

    void setFitness(double value);

    Object getChromosomeRow(int row);

    Object getChromosomeColumn(int column);

    void setChromosomeColumn(int column, Object[] data);

    void setChromosomeRow(int row, Object[] data);
}
