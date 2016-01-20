package hr.fer.tki.evolution_algorithm.crossover;

import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;

import java.util.List;

public interface ICrossover {

	List<IChromosome> crossover(IChromosome chrom1, IChromosome chrom2);

}
