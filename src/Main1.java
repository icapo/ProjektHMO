import hr.fer.tki.evolution_algorithm.GeneticAlgorithm;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfo;

public class Main1 {

	public static void main(String[] args) {
		TaskInfo taskInfo = new TaskInfo();
		
		taskInfo.parse("C:/Users/gabriel/workspaceHMO/Scheduling/src/sample.txt");
		
		System.out.println(taskInfo.getNumberOfDays());
		
		GeneticAlgorithm GA = new GeneticAlgorithm(1, taskInfo);
	}

}
