package hr.fer.tki.evolution_algorithm.mutation;


import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfo;
import hr.fer.tki.functions.ConstraintChecker;

import java.util.Random;

public class Mutation implements IMutation {

    private TaskInfo taskInfo;
    private double mutationCofficient;

    public Mutation(TaskInfo taskInfo, double mutationCofficient) {
        this.taskInfo = taskInfo;
        this.mutationCofficient = mutationCofficient;
    }

    @Override
    public IChromosome copyMutate(IChromosome chrom) {
        return this.mutate(chrom.copy());
    }

    @Override
    public IChromosome mutate(IChromosome chromosome) {
        //mutate but ignore invalid solution
        return this.mutateByDay(chromosome);
    }

    private IChromosome mutateByDay(IChromosome chromosome) {
        Random random = new Random();

        if (random.nextFloat() < 0.1) {

            int cols = chromosome.getColsNum();
            for (int i = 0; i < chromosome.getRowsNum(); i++) {
                int first = random.nextInt(cols);
                int second = random.nextInt(cols);

                String firstElement = (String) chromosome.getChromosomeElement(i, first);
                String secondElement = (String) chromosome.getChromosomeElement(i, second);

                chromosome.setChromosomeElement(i, first, secondElement);
                chromosome.setChromosomeElement(i, second, firstElement);

                if (!ConstraintChecker.checkHardConstraints(chromosome, this.taskInfo)) {
                    chromosome.setChromosomeElement(i, first, firstElement);
                    chromosome.setChromosomeElement(i, second, secondElement);
                }
            }
        }
        return chromosome;

    }
}

