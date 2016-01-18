import hr.fer.tki.evolution_algorithm.GeneticAlgorithm;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfo;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfoParser;

public class Main1 {

	public static void main(String[] args) {
		TaskInfo taskInfo = TaskInfoParser.parse("/Users/filip/Projects/hmo/ProjektHMO/sample.txt");
		
		System.out.println(taskInfo.getNumberOfDays());
		
		GeneticAlgorithm GA = new GeneticAlgorithm(1, taskInfo);
	}

}
