package hr.fer.tki.functions;

import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;

public interface IFitnessFunction {

	public double calculate(IChromosome chrom);
	
}
