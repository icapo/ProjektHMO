package hr.fer.zemris.evolution_algorithm.crossover;

import hr.fer.zemris.evolution_algorithm.chromosome.IChromosome;

public interface ICrossover {

	IChromosome crossover(IChromosome chrom1, IChromosome chrom2);

}
