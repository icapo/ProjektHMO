package hr.fer.tki.evolution_algorithm.selection;

import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;

import java.awt.*;
import java.util.*;
import java.util.List;


public class TournamentSelection implements  ISelection{

    private int numberOfResults = 10;

    public TournamentSelection(int numberOfResults) {
        this.numberOfResults = numberOfResults;
    }

    @Override
    public List<IChromosome> doSelection(List<IChromosome> chromosomes) {
        Collections.sort(chromosomes, new Comparator<IChromosome>() {
            @Override
            public int compare(IChromosome o1, IChromosome o2) {
                return (int) (o1.getFitness() - o2.getFitness());
            }
        });

        List<IChromosome> solutions = new LinkedList<>();
        solutions.add(chromosomes.get(0));

        Random r = new Random();
        int i = 1;
        for (; i < this.numberOfResults || i < chromosomes.size(); i++) {
            if (r.nextFloat() < 0.05) {
                continue;
            }
            solutions.add(chromosomes.get(i));
        }

        for (int j = i; j < chromosomes.size() || j < this.numberOfResults; j++) {
            solutions.add(chromosomes.get(j));
        }

        return solutions;
    }
}
