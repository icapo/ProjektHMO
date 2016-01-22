package hr.fer.tki.evolution_algorithm.selection;

import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;
import hr.fer.tki.evolution_algorithm.genetic.GeneticAlgorithm;

import java.util.*;
import java.util.List;


public class TournamentSelection implements  ISelection{

    private double percentage = 0.9;

    public TournamentSelection(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public List<IChromosome> doSelection(List<IChromosome> chromosomes, int count) {

        GeneticAlgorithm.sortByFitness(chromosomes);
        List<IChromosome> solutions = new LinkedList<>();

        for (int i = 0; i < count; i++) {
            solutions.add(chromosomes.get(i));
        }

        return solutions;
    }
}
