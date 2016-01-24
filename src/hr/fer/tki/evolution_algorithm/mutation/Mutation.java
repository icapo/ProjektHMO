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

        if (random.nextFloat() < this.mutationCofficient) {
            int days = random.nextInt(6) + 1;
            int cols = chromosome.getColsNum();
            //foreach person
            for (int i = 0; i < chromosome.getRowsNum(); i++) {

                int first = 0;
                int second = first + days - 1;

                while (Math.abs(second - first) < days) {
                    first = random.nextInt(cols / days);
                    second = random.nextInt(cols / days);
                }

                for (int j = 0; j < days; j++) {

                    String firstElement = (String) chromosome.getChromosomeElement(i, first + j);
                    String secondElement = (String) chromosome.getChromosomeElement(i, second + j);

                    chromosome.setChromosomeElement(i, first + j, secondElement);
                    chromosome.setChromosomeElement(i, second + j, firstElement);
                }

                if (!ConstraintChecker.checkHardConstraints(chromosome, this.taskInfo)) {
                    for (int j = 0; j < days; j++) {
                        String firstElement = (String) chromosome.getChromosomeElement(i, first + j);
                        String secondElement = (String) chromosome.getChromosomeElement(i, second + j);

                        chromosome.setChromosomeElement(i, first + j, secondElement);
                        chromosome.setChromosomeElement(i, second + j, firstElement);
                    }
                }
            }
        }
        return chromosome;

    }
}

