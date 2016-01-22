import java.util.List;

import hr.fer.tki.evolution_algorithm.crossover.Crossover;
import hr.fer.tki.evolution_algorithm.genetic.GeneticAlgorithm;
import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;
import hr.fer.tki.evolution_algorithm.mutation.Mutation;
import hr.fer.tki.evolution_algorithm.selection.TournamentSelection;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfo;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfoParser;
import hr.fer.tki.functions.FitnessFunction;

public class Main1 {

	public static void main(String[] args) {
		int populationSize = 100;
		int bestSolutionsNum = 10;
        int epochSize = 40;

		double crossover = 0.6;
		double delta = 50;

		TaskInfo taskInfo = TaskInfoParser.parse("sample.txt");
		
		GeneticAlgorithm GA = new GeneticAlgorithm(new FitnessFunction(), new Mutation(taskInfo), new Crossover(crossover), new TournamentSelection(0.9) ,epochSize, delta, populationSize, taskInfo);
        GA.startTraining();
		System.out.println("Population generated");
		List<IChromosome> bestSolutions = GA.getBestSolutions(bestSolutionsNum);
		for (int i = 0; i < bestSolutionsNum; i++) {
			TaskInfoParser.write(bestSolutions.get(i), "res-" + (i + 1) + "-capalija-gobin.txt");
		}
	}
}
