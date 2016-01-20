package hr.fer.tki.evolution_algorithm.selection;

import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;

import java.awt.*;
import java.util.*;
import java.util.List;


public class TournamentSelection implements  ISelection{

    private double percentage = 0.9;

    public TournamentSelection(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public List<IChromosome> doSelection(List<IChromosome> chromosomes) {

        Collections.sort(chromosomes, (o1, o2) -> (int) (o1.getFitness() - o2.getFitness()));

        List<IChromosome> solutions = new LinkedList<>();


        for (int i = 0; i <= (this.percentage * chromosomes.size()); i++) {
            solutions.add(chromosomes.get(i));
        }

        return solutions;
    }
}
