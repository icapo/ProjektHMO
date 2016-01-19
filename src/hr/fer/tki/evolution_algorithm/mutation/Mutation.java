package hr.fer.tki.evolution_algorithm.mutation;


import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;

import java.util.Random;

public class Mutation implements IMutation {

    @Override
    public IChromosome copyMutate(IChromosome chrom) {
        return this.mutate(chrom.copy());
    }

    @Override
    public IChromosome mutate(IChromosome chrom) {

        Random random = new Random();
        int cols = chrom.getColsNum();
        int rows = chrom.getRowsNum();

        for (int j = 0; j < chrom.getColsNum(); j++) {
            for (int i = 0; i < chrom.getRowsNum(); i++) {
                if (random.nextFloat() < 0.005) {
                    String element = (String) chrom.getChromosomeElement(i, j);
                    chrom.setChromosomeElement(j % rows, i % cols, element);
                }
            }
        }
        return chrom;
    }
}
