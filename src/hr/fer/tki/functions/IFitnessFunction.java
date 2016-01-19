package hr.fer.tki.functions;

import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfo;

public interface IFitnessFunction {

	public double calculate(IChromosome chromosome, TaskInfo taskInfo);
	
}
