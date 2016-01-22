package hr.fer.tki.evolution_algorithm.genetic;

import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;
import hr.fer.tki.evolution_algorithm.crossover.ICrossover;
import hr.fer.tki.evolution_algorithm.mutation.IMutation;
import hr.fer.tki.evolution_algorithm.selection.ISelection;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfo;
import hr.fer.tki.functions.ConstraintChecker;
import hr.fer.tki.functions.IFitnessFunction;

import java.util.*;
import java.util.stream.Collectors;

public class GeneticAlgorithm {

    private IMutation mutation;
    private ISelection selection;
    private ICrossover crossover;
    private int epochNum;
    private int populationSize;
    private IFitnessFunction fitnessFunction;
    private IChromosome startChromosome;
    private boolean binaryType;
    private List<IChromosome> population;
    private double delta;
    private TaskInfo taskInfo;


    public GeneticAlgorithm(IFitnessFunction fitnessFunction,
                            IMutation mutation, ICrossover crossover, ISelection selection, int epochNum,
                            double delta, int populationSize, TaskInfo taskInfo) {
        super();
        this.mutation = mutation;
        this.crossover = crossover;
        this.selection = selection;
        this.epochNum = epochNum;
        this.fitnessFunction = fitnessFunction;
        this.binaryType = binaryType;
        this.delta = delta;
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
        List<IChromosome> solutions = new LinkedList<>();

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

    public void evaluateChromosome(IChromosome chromosome) {
        double fitness = this.fitnessFunction.calculate(chromosome, this.taskInfo);
        chromosome.setFitness(fitness);
    }


    /**
     * Method starts training genetic algorithm
     */
    public void startTraining() {
        //generate starting populataion
        this.population = PopulationGenerator.generateStartingPopulation(this.populationSize * 10, taskInfo);
        //evaluate starting population
        this.evaluatePopulation(this.population);

        //sort starting population
        GeneticAlgorithm.sortByFitness(this.population);
        List<IChromosome> currentPopulation = new LinkedList<>();
        currentPopulation.addAll(this.population.stream().collect(Collectors.toList()));


        System.out.println("Starting population BEST:---- ");
        System.out.println(currentPopulation.get(0).getFitness() + "," + currentPopulation.get(1).getFitness() +
                "," + currentPopulation.get(currentPopulation.size() - 1).getFitness());
        System.out.println();

        for (int epochNumber = 0; epochNumber < this.epochNum; epochNumber++) {

            List<IChromosome> newPopulation = new LinkedList<>();

            double minFitness = 89999999;

            /***
             * Create new population
             */
            for (int i = 0; i < this.populationSize; i++) {

                IChromosome chromosome1 = this.selection.doSelection(this.population);
                IChromosome chromosome2 = this.selection.doSelection(this.population);

                /***
                 * CROSSOVER
                 */
                List<IChromosome> result = this.crossover.crossover(chromosome1, chromosome2);

                /**
                 * Two chromosomes created by crossover
                 */
                for (IChromosome c : result) {

                    if (!ConstraintChecker.checkHardConstraints(c, this.taskInfo)) {
                        //check if ever happening
                        System.out.println("HARD CONSTRAINTS VIOLATED BY CROSSOVER");
                        continue;
                    }

                    /**
                     * MUTATION
                     */
                    c = this.mutation.mutate(c);

                    /**
                     * CALCULATE FITNESS
                     */
                    double fitness = this.fitnessFunction.calculate(c, this.taskInfo);
                    if (fitness < minFitness) {
                        minFitness = fitness;
                    }

                    c.setFitness(fitness);
                    newPopulation.add(c);

                }
            }
            currentPopulation = currentPopulation.subList(0, (int) (0.1 * this.populationSize));
            //add all new population to current population
            currentPopulation.addAll(newPopulation.subList(0, (int) (0.9*this.populationSize)).stream().collect(Collectors.toList()));
            newPopulation.clear();
            GeneticAlgorithm.sortByFitness(currentPopulation);

            //get best population
            currentPopulation = new LinkedList<>(currentPopulation.subList(0, this.populationSize));


            System.out.println("Number: " + epochNumber + " population " + currentPopulation.size() + " BEST :--Crossover,mutation: " + minFitness);
            System.out.println(currentPopulation.get(0).getFitness() + ","
                    + currentPopulation.get(1).getFitness() + ","
                    + currentPopulation.get(2).getFitness() + " - "
                    + currentPopulation.get(currentPopulation.size() - 1).getFitness());

        }

        this.population = currentPopulation;
        System.out.println("ENDING population ---- ");
        for (int i = 0; i < this.population.size(); i++) {
            System.out.println(this.population.get(i).getFitness());
        }
    }


    public static void sortByFitness(List<IChromosome> chromosomes) {
        Collections.sort(chromosomes, (o1, o2) -> (int) (o1.getFitness() - o2.getFitness()));
    }
}
