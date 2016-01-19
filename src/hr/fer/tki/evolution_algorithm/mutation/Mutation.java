package hr.fer.tki.evolution_algorithm.mutation;


import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;

public class Mutation implements IMutation {

    @Override
    public IChromosome copyMutate(IChromosome chrom) {
       return this.mutate(chrom.copy());
    }

    @Override
    public IChromosome mutate(IChromosome chrom) {


        return chrom;
    }
}
