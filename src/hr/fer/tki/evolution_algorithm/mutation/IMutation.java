package hr.fer.tki.evolution_algorithm.mutation;

import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;

public interface IMutation {

	/**
	 * Mutates copy of given chromosome object <code>chrom</code>.
	 * @param chrom
	 * @return mutated copy of given chromosome object.
	 */
	IChromosome copyMutate(IChromosome chrom);
	
	/**
	 * Mutates given chromosome object <code>chrom</code>.
	 * @param chrom
	 * @return same but mutated chromosome object.
	 */
	IChromosome mutate(IChromosome chrom);
	
}
