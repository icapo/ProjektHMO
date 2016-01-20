package hr.fer.tki.evolution_algorithm.genetic;

import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;
import hr.fer.tki.evolution_algorithm.crossover.ICrossover;
import hr.fer.tki.evolution_algorithm.mutation.IMutation;
import hr.fer.tki.evolution_algorithm.selection.ISelection;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfo;
import hr.fer.tki.functions.ConstraintChecker;
import hr.fer.tki.functions.IFitnessFunction;

import java.util.*;

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
    private double precision;
    private TaskInfo taskInfo;


    public GeneticAlgorithm(IFitnessFunction fitnessFunction,
                            IMutation mutation, ICrossover crossover, ISelection selection, int epochNum,
                            double precision, int populationSize, TaskInfo taskInfo) {
        super();
        this.mutation = mutation;
        this.crossover = crossover;
        this.selection = selection;
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
        this.population = PopulationGenerator.generateStartingPopulation(populationSize, taskInfo);
        this.evaluatePopulation(this.population);
        sortByFitness(this.population);

        List<IChromosome> currentPopulation = this.population;

        System.out.println("Starting population BEST:---- ");
        System.out.println(currentPopulation.get(0).getFitness()+","+currentPopulation.get(1).getFitness()+","+currentPopulation.get(2).getFitness());
        System.out.println();

        for (int i = 0; i < this.epochNum; i++) {

            int populationSize = currentPopulation.size();

            List<IChromosome> newPopulation = new LinkedList<>();

            double minFitness = 89999999;
            for (int j = 0; j < populationSize - 1; j++) {
                for (int k = j+1; k < populationSize; k++) {

                    IChromosome chromosome1 = currentPopulation.get(j);
                    IChromosome chromosome2 = currentPopulation.get(k);
                    List<IChromosome>  result = this.crossover.crossover(chromosome1, chromosome2);

                    for (IChromosome c : result) {
                        if(c == null) {
                            continue;
                        }
                        if(!ConstraintChecker.checkHardConstraints(c, this.taskInfo)) {
                            continue;
                        }
                        double fitness = this.fitnessFunction.calculate(c, this.taskInfo);
                        if(fitness < minFitness) {
                            minFitness = fitness;
                        }
                        System.out.println("dfasafdsfdasfdasfadsdfadsfjdhsfkjadshfkjds: " + fitness);

                        c.setFitness(fitness);
                        newPopulation.add(c);
                    }
                }
            }

            for (IChromosome c : newPopulation) {
                currentPopulation.add(c);
            }
            newPopulation.clear();

            //try mutating some of the population - like 3% of population

            currentPopulation = this.selection.doSelection(currentPopulation, this.populationSize);
            System.out.println("Number: "+ i +" population BEST GENERATED:---- " + minFitness);
            System.out.println(currentPopulation.get(0).getFitness()+","+currentPopulation.get(1).getFitness()+","+currentPopulation.get(2).getFitness());

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
