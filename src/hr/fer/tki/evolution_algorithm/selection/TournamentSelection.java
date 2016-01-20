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

        solutions.add(chromosomes.get(0));
        double lastFitness = chromosomes.get(0).getFitness();

        for (int i = 1; i < count && i < chromosomes.size(); i++) {
            if(lastFitness == chromosomes.get(i).getFitness()) {
                count++;
                continue;
            }
            lastFitness = chromosomes.get(i).getFitness();
            solutions.add(chromosomes.get(i));
        }

        return solutions;
    }
}
