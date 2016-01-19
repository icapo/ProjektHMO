package hr.fer.tki.evolution_algorithm.genetic;

import java.util.Random;

import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;
import hr.fer.tki.evolution_algorithm.chromosome.TableChromosome;
import hr.fer.tki.evolution_algorithm.crossover.ICrossover;
import hr.fer.tki.evolution_algorithm.mutation.IMutation;
import hr.fer.tki.evolution_algorithm.task_info.EmployeeInfo;
import hr.fer.tki.evolution_algorithm.task_info.Shift;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfo;
import hr.fer.tki.functions.ConstraintChecker;
import hr.fer.tki.functions.IFitnessFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class GeneticAlgorithm {

    private IMutation mutation;
    private ICrossover crossover;
    private int epochNum;
    private int populationSize;
    private IFitnessFunction func;
    private IChromosome startChromosome;
    private boolean binaryType;
    private List<IChromosome> population;
    private double precision;
    private TaskInfo taskInfo;


    public GeneticAlgorithm(IFitnessFunction func,
                            IMutation mutation, ICrossover crossover, int epochNum,
                            double precision, int populationSize, TaskInfo taskInfo) {
        super();
        this.mutation = mutation;
        this.crossover = crossover;
        this.epochNum = epochNum;
        this.func = func;
        this.binaryType = binaryType;
        this.precision = precision;
        this.taskInfo = taskInfo;
        this.population = PopulationGenerator.generatePopulation(populationSize, taskInfo);

        // check if hard constraints are satisfied
        for (IChromosome chrom : population) {
            if (!ConstraintChecker.checkHardConstraints(chrom, taskInfo)) {
                System.out.println("Hard constraints are not satisfied.");
                System.exit(1);
            }
        }
    }

    /**
     * Get best solutions.
     *
     * @param num
     * @return
     */
    public List<IChromosome> getBestSolutions(int num) {
        List<IChromosome> solutions = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            solutions.add(population.get(i));
        }
        return solutions;
    }


}
