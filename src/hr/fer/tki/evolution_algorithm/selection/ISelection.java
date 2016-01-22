package hr.fer.tki.evolution_algorithm.selection;


import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;

import java.util.List;

public interface ISelection {

    public IChromosome doSelection(List<IChromosome> chromosomes);
}
