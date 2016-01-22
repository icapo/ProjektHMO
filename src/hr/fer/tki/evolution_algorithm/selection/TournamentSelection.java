package hr.fer.tki.evolution_algorithm.selection;

import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;

import java.util.*;
import java.util.List;


public class TournamentSelection implements ISelection {

    private double probability = 0.5;

    public TournamentSelection(double percentage) {
        this.probability = percentage;
    }

    @Override
    public IChromosome doSelection(List<IChromosome> chromosomes) {

        Random random = new Random();
        IChromosome solution = null;

        double alpha = this.probability;
        double step = (alpha - 0.005) / chromosomes.size();

        for (int i = 0; i < chromosomes.size(); i++) {
            /**
             * Classical tournament
             */
//            alpha = this.probability * Math.pow(1 - this.probability, i);
//            if (random.nextFloat() < alpha) {
//                solution = chromosomes.get(i);
//                break;
//            }
            /**
             * Adjusted alpha
             */
            alpha = alpha - step;
            if (random.nextFloat() < alpha) {
                solution = chromosomes.get(i);
                break;
            }
        }


        if (solution == null) {
            solution = chromosomes.get(0);
        }
        return solution.copy();
    }
}
