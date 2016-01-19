package hr.fer.tki.evolution_algorithm.genetic;

import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;
import hr.fer.tki.evolution_algorithm.crossover.ICrossover;
import hr.fer.tki.evolution_algorithm.mutation.IMutation;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfo;
import hr.fer.tki.functions.ConstraintChecker;
import hr.fer.tki.functions.IFitnessFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GeneticAlgorithm {

    private IMutation mutation;
    private ICrossover crossover;
    private int epochNum;
    private int populationSize;
    private IFitnessFunction fitnessFunction;
    private IChromosome startChromosome;
    private boolean binaryType;
    private List<IChromosome> population;
    private double precision;
    private TaskInfo taskInfo;


    public GeneticAlgorithm(IFitnessFunction fitnessFunction,
                            IMutation mutation, ICrossover crossover, int epochNum,
                            double precision, int populationSize, TaskInfo taskInfo) {
        super();
        this.mutation = mutation;
        this.crossover = crossover;
        this.epochNum = epochNum;
        this.fitnessFunction = fitnessFunction;
        this.binaryType = binaryType;
        this.precision = precision;
        this.taskInfo = taskInfo;
        this.populationSize = populationSize;

    }

    /**
     * Get best solutions.
     *
     * @param num
     * @return
     */
    public List<IChromosome> getBestSolutions(int num) {
        List<IChromosome> solutions = new ArrayList<>();

        this.evaluatePopulation(this.population);

        this.sortPopulation();

        for (int i = 0; i < num; i++) {
            solutions.add(this.population.get(i));
        }

        for (IChromosome chromosome : solutions) {
            System.out.println(chromosome.getFitness());
        }
        return solutions;
    }

    public void evaluatePopulation(List<IChromosome> population) {
        for (int i = 0; i < population.size(); i++) {
            IChromosome chromosome = population.get(i);
            double fitness = this.fitnessFunction.calculate(chromosome, this.taskInfo);
            chromosome.setFitness(fitness);
        }
    }


    private void sortPopulation() {
        Collections.sort(this.population, new Comparator<IChromosome>() {
            @Override
            public int compare(IChromosome o1, IChromosome o2) {
                return (int) (o1.getFitness() - o2.getFitness());
            }
        });
    }


    /**
     * Method starts training genetic algorithm
     */
    public void startTraining() {
        this.population = PopulationGenerator.generateStartingPopulation(populationSize, taskInfo);
    }
}
