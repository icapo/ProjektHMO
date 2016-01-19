import java.util.List;

import hr.fer.tki.evolution_algorithm.genetic.GeneticAlgorithm;
import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfo;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfoParser;

public class Main1 {

	public static void main(String[] args) {
		int populationSize = 30;
		int bestSolutionsNum = 10;
		TaskInfo taskInfo = TaskInfoParser.parse("sample.txt");
		
		GeneticAlgorithm GA = new GeneticAlgorithm(null, null, null, 0, 0, populationSize, taskInfo);
		System.out.println("Population generated");
		List<IChromosome> bestSolutions = GA.getBestSolutions(bestSolutionsNum);
		for (int i = 0; i < bestSolutionsNum; i++) {
			TaskInfoParser.write(bestSolutions.get(i), "res-" + (i + 1) + "-capalija.txt");
		}
	}
}
