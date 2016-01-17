package hr.fer.zemris.evolution_algorithm;

import java.util.Random;

import hr.fer.zemris.evolution_algorithm.chromosome.IChromosome;
import hr.fer.zemris.evolution_algorithm.chromosome.TableChromosome;
import hr.fer.zemris.evolution_algorithm.crossover.ICrossover;
import hr.fer.zemris.evolution_algorithm.mutation.IMutation;
import hr.fer.zemris.evolution_algorithm.task_info.EmployeeInfo;
import hr.fer.zemris.evolution_algorithm.task_info.Shift;
import hr.fer.zemris.evolution_algorithm.task_info.TaskInfo;
import hr.fer.zemris.functions.IFitnessFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {

	private IMutation mutation;
	private ICrossover crossover;
	private int stopNum;
	private int populationSize;
	private IFitnessFunction func;
	private IChromosome startChromosome;
	private boolean binaryType;
	private List<IChromosome> population;
	private double precision;
	private TaskInfo taskInfo;

	public GeneticAlgorithm(int populationSize, TaskInfo taskInfo) {
		super();
		this.taskInfo = taskInfo;
		this.population = generatePopulation(populationSize);
	}

	public GeneticAlgorithm(IFitnessFunction func, boolean binaryType,
			IMutation mutation, ICrossover crossover, int stopNum,
			double precision, int populationSize, TaskInfo taskInfo) {
		super();
		this.mutation = mutation;
		this.crossover = crossover;
		this.stopNum = stopNum;
		this.func = func;
		this.binaryType = binaryType;
		this.precision = precision;
		this.taskInfo = taskInfo;
		this.population = generatePopulation(populationSize);
	}

	private List<Shift> getAllowedShifts(
			HashMap<Shift, Integer> currEmployeeShifts, int daysNum,
			Shift lastShift) {
		Random rand = new Random();
		int randRounds = 100;
		List<Shift> usefulShifts = new ArrayList<Shift>();
		List<Integer> usefulShiftsNum = new ArrayList<Integer>();

		// check if there is a valid solution
		int daysCounter = 0;
		for (Shift shift : currEmployeeShifts.keySet()) {
			int maxShifts = currEmployeeShifts.get(shift);
			daysCounter += maxShifts;

			if (currEmployeeShifts.get(shift) > 0) {
				usefulShifts.add(shift);
				usefulShiftsNum.add(maxShifts);
			}
		}
		if (daysCounter < daysNum) {
			return null;
		}

		// find suggestion
		for (int round = 0; round < randRounds; round++) {
			List<Shift> allowedShifts = new ArrayList<Shift>();
			List<Shift> currShifts = (List<Shift>) ((ArrayList) usefulShifts)
					.clone();
			List<Integer> currShiftsNum = (List<Integer>) ((ArrayList) usefulShiftsNum)
					.clone();

			for (int dayIndex = 0; dayIndex < daysNum; dayIndex++) {
				int shiftIndex = rand.nextInt(currShifts.size());
				Shift shift = currShifts.get(shiftIndex);

				if (dayIndex == 0) {
					if (lastShift != null && !lastShift.checkIfCanFollow(shift)) {
						break;
					}
				} else {
					// if cannot follow break
					if (!allowedShifts.get(dayIndex - 1)
							.checkIfCanFollow(shift)) {
						break;
					}
				}
				allowedShifts.add(shift);

				// update days
				currShiftsNum
						.set(shiftIndex, currShiftsNum.get(shiftIndex) - 1);

				// check if not usefull
				if (currShiftsNum.get(shiftIndex) <= 0) {
					currShiftsNum.remove(shiftIndex);
					currShifts.remove(shiftIndex);
				}
			}

			// if solution has been found
			if (allowedShifts.size() == daysNum) {
				return allowedShifts;
			}
		}

		// if was unsuccessful
		return null;
	}

	private List<IChromosome> generatePopulation(int populationSize) {
		List<IChromosome> localPopulation = new ArrayList<IChromosome>();

		for (int chromIndex = 0; chromIndex < populationSize; chromIndex++) {
			String[][] data = new String[taskInfo.getStaff().size()][taskInfo
					.getNumberOfDays()];

			// iterate employees
			for (EmployeeInfo currEmployee : taskInfo.getStaff().values()) {
				// employee current state variables
				HashMap<Shift, Integer> currEmployeeShifts = (HashMap<Shift, Integer>) currEmployee
						.getMaxShifts().clone();
				int currTotalMinutes = 0;
				int currWeekends = 0;
				Shift lastShift;

				Random rand = new Random();
				int startingDay = rand.nextInt(2);

				// iterate days
				for (int dayIndex = startingDay; dayIndex < taskInfo
						.getNumberOfDays(); dayIndex++) {

					if (dayIndex > 140
							&& currTotalMinutes < currEmployee
									.getMinTotalMinutes()) {
						System.out.println("tu");
					}

					lastShift = null;
					if (dayIndex > 0) {
						// if is not day off
						if (data[currEmployee.getEmployeeIndex()][dayIndex - 1] != null) {
							lastShift = taskInfo.getShift(data[currEmployee
									.getEmployeeIndex()][dayIndex - 1]);
						} else if (dayIndex != 1) {// if it is day of add min days off
							dayIndex += currEmployee.getMinConsecutiveDaysOff();
						}
					}

					
					int shiftsNum = currEmployee.getMinConsecutiveShifts();
					
					// suggest shifts
					List<Shift> suggestedShifts = getAllowedShifts(
							currEmployeeShifts, shiftsNum, lastShift);

					// if getAllowedShifts failed to find suggestion for
					// shifts order
					if (suggestedShifts == null) {
						continue;
					}

					// /////// check if allowed //////////

					// check shift size
					/*if (suggestedShifts.size() > currEmployee
							.getMaxConsecutiveShifts()
							|| suggestedShifts.size() < currEmployee
									.getMinConsecutiveShifts()) {
						continue;
					}*/

					// check max total minutes
					int totalMinutes = currTotalMinutes;
					for (Shift s : suggestedShifts) {
						totalMinutes += s.getLengthInMins();
					}

					if (totalMinutes > currEmployee.getMaxTotalMinutes()) {
						continue;
					}

					// check weekends and days off
					int weekends = 0;
					boolean daysOff = false;
					for (int i = dayIndex; i < dayIndex + shiftsNum; i++) {

						// check if there is a weekend in this range
						if (i == 0 || i % 7 == 6) {
							weekends++;
						}

						// if there is days off in this range
						if (Collections.binarySearch(
								currEmployee.getDaysOff(), i) >= 0) {
							daysOff = true;
							break;
						}
					}

					if (weekends + currWeekends > currEmployee
							.getMaxWeekends()) {
						continue;
					}

					// if there is days off in given range
					if (daysOff) {
						continue;
					}

					// //// if there is no errors, add shifts to employee
					// ///////

					for (int i = dayIndex; i < dayIndex + shiftsNum; i++) {
						Shift suggestedShift = suggestedShifts.get(i
								- dayIndex);
						currEmployeeShifts.put(suggestedShift,
								currEmployeeShifts.get(suggestedShift) - 1);
						data[currEmployee.getEmployeeIndex()][i] = suggestedShift
								.getShiftID();
						currTotalMinutes += suggestedShift
								.getLengthInMins();
					}

					currWeekends += weekends;
					dayIndex += shiftsNum - 1;
					

				}
				System.out.println(currEmployee.getEmployeeID());
				for (int k = 0; k < taskInfo.getNumberOfDays(); k++)
					System.out.print("(" + k + ":"
							+ data[currEmployee.getEmployeeIndex()][k] + "),");
				System.out.println();
				if (!(currEmployee.getMaxTotalMinutes() >= currTotalMinutes
						&& currTotalMinutes >= currEmployee
						.getMinTotalMinutes())) {
					return null;
				}
				System.out.println(currTotalMinutes);
				for (Shift s : currEmployeeShifts.keySet()) {
					System.out.print("(" + s.getShiftID() + ":"
							+ currEmployeeShifts.get(s) + ")");
				}
				System.out.println();
				System.out.println();
			}

			localPopulation.add(new TableChromosome(data));
			System.out.println("KRAAAAAAAAA\n\n\n\n\n");
		}
		return localPopulation;
	}

}
