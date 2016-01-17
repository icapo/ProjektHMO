package hr.fer.zemris.functions;

import hr.fer.zemris.evolution_algorithm.chromosome.IChromosome;

public interface IFitnessFunction {

	public double calculate(IChromosome chrom);
	
}
