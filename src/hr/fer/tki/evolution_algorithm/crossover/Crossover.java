package hr.fer.tki.evolution_algorithm.crossover;

import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Crossover implements ICrossover {


    public Crossover() {
    }

    @Override
    public List<IChromosome> crossover(IChromosome chromosome1, IChromosome chromosome2) {

        //do day switch
        Random random = new Random();
        return this.doRowCrossover(chromosome1, chromosome2, random);
    }

    /**
     * Deprecated
     *
     * @param chromosome1
     * @param chromosome2
     * @param random
     * @return
     */
    private List<IChromosome> doColumnCrossover(IChromosome chromosome1, IChromosome chromosome2, Random random) {

        List<IChromosome> list = new LinkedList<>();

        int chromosome1ColsNum = chromosome1.getColsNum();
        int chromosome2ColsNum = chromosome2.getColsNum();
        int col1Index = random.nextInt(chromosome1ColsNum);
        int col2Index = random.nextInt(chromosome2ColsNum);
        Object[] col1 = (Object[]) chromosome1.getChromosomeColumn(col1Index);
        Object[] col2 = (Object[]) chromosome2.getChromosomeColumn(col2Index);

        chromosome1.setChromosomeColumn(col2Index, col2);
        chromosome2.setChromosomeColumn(col1Index, col1);
        list.add(chromosome1);
        list.add(chromosome2);
        return list;

    }

    private List<IChromosome> doRowCrossover(IChromosome chromosome1, IChromosome chromosome2, Random random) {

        List<IChromosome> list = new LinkedList<>();

        int chromosome1RowsNum = chromosome1.getRowsNum();
        int chromosome2RowsNum = chromosome2.getRowsNum();
        int row1Index = random.nextInt(chromosome1RowsNum);
        int row2Index = random.nextInt(chromosome2RowsNum);

        Object[] col1 = (Object[]) chromosome1.getChromosomeRow(row1Index);
        Object[] col2 = (Object[]) chromosome2.getChromosomeRow(row2Index);

        chromosome1.setChromosomeRow(row2Index, col2);
        chromosome2.setChromosomeRow(row1Index, col1);
        list.add(chromosome1);
        list.add(chromosome2);

        //do repair if failed
        return list;

    }
}
