package hr.fer.tki.evolution_algorithm.mutation;


import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfo;
import hr.fer.tki.functions.ConstraintChecker;

import java.util.Random;

public class Mutation implements IMutation {

    private TaskInfo taskInfo;

    public Mutation(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    @Override
    public IChromosome copyMutate(IChromosome chrom) {
        return this.mutate(chrom.copy());
    }

    @Override
    public IChromosome mutate(IChromosome chromosome) {

        return this.mutateByDay(chromosome);
    }

    private IChromosome mutateByDay(IChromosome chromosome) {
        Random random = new Random();

        if (random.nextFloat() < 0.1) {
            IChromosome chrom = chromosome.copy();
            boolean mutationHappened = false;

            int cols = chromosome.getColsNum();
            for (int i = 0; i < chrom.getRowsNum(); i++) {
                int first = random.nextInt(cols);
                int second = random.nextInt(cols);

                String firstElement = (String) chrom.getChromosomeElement(i, first);
                String secondElement = (String) chrom.getChromosomeElement(i, second);

                chrom.setChromosomeElement(i, first, secondElement);
                chrom.setChromosomeElement(i, second, firstElement);

                if (!ConstraintChecker.checkHardConstraints(chrom, this.taskInfo)) {
                    chrom.setChromosomeElement(i, first, firstElement);
                    chrom.setChromosomeElement(i, second, secondElement);
                } else {
                    mutationHappened = true;
                }
            }

            if (mutationHappened) {
                return chrom;
            } else {
                return null;
            }

        } else {
            return null;
        }
    }
}

