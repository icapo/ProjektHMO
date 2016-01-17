package hr.fer.tki.evolution_algorithm.crossover;

import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;

public interface ICrossover {

	IChromosome crossover(IChromosome chrom1, IChromosome chrom2);

}
