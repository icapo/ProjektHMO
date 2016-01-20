package hr.fer.tki.evolution_algorithm.crossover;

import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Crossover implements  ICrossover{

    double coeff = 0.1;

    public Crossover(double coeff) {
     this.coeff = coeff;
    }

    @Override
    public List<IChromosome> crossover(IChromosome chromosome1, IChromosome chromosome2) {
        //do day switch
        Random random = new Random();

        List<IChromosome> list = new LinkedList<>();
        double num = random.nextFloat();
//        if (num < (this.coeff / 1000.0)) {
//            List<IChromosome> chromosomes = this.doColumnCrossover(random, chromosome1, chromosome2);
//            list.add(chromosomes.get(0).copy());
//            list.add(chromosomes.get(1).copy());
//        } else
        if (num < this.coeff) {
            List<IChromosome> chromosomes = this.doRowCrossover(random, chromosome1, chromosome2);
            list.add(chromosomes.get(0).copy());
            list.add(chromosomes.get(1).copy());
        } else {
            list.add(null);
            list.add(null);
        }

        //do repair if failed
        return list;
    }

    private List<IChromosome> doColumnCrossover(Random random, IChromosome chromosome1, IChromosome chromosome2) {
        int chromosome1ColsNum = chromosome1.getColsNum();
        int chromosome2ColsNum = chromosome2.getColsNum();
        int col1Index = random.nextInt(chromosome1ColsNum);
        int col2Index = random.nextInt(chromosome2ColsNum);
        Object[] col1 = (Object[]) chromosome1.getChromosomeColumn(col1Index);
        Object[] col2 = (Object[]) chromosome2.getChromosomeColumn(col2Index);

        chromosome1.setChromosomeColumn(col2Index, col2);
        chromosome2.setChromosomeColumn(col1Index, col1);
        List<IChromosome> chromosomes = new LinkedList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        return chromosomes;
    }

    private List<IChromosome> doRowCrossover(Random random, IChromosome chromosome1, IChromosome chromosome2) {
        int chromosome1RowsNum = chromosome1.getRowsNum();
        int chromosome2RowsNum = chromosome2.getRowsNum();
        int row1Index = random.nextInt(chromosome1RowsNum);
        int row2Index = random.nextInt(chromosome2RowsNum);

        Object[] col1 = (Object[]) chromosome1.getChromosomeRow(row1Index);
        Object[] col2 = (Object[]) chromosome2.getChromosomeRow(row2Index);

        chromosome1.setChromosomeRow(row2Index, col2);
        chromosome2.setChromosomeRow(row1Index, col1);
        List<IChromosome> chromosomes = new LinkedList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        return chromosomes;
    }
}
