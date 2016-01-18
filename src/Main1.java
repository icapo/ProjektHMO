import hr.fer.tki.evolution_algorithm.GeneticAlgorithm;
import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfo;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfoParser;

public class Main1 {

	public static void main(String[] args) {
		TaskInfo taskInfo = TaskInfoParser.parse("sample.txt");
		
		GeneticAlgorithm GA = new GeneticAlgorithm(null, null, null, 0, 0, 30, taskInfo);	
		System.out.println("Population generated");
	}
}
