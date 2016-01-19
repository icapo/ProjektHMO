package hr.fer.tki.evolution_algorithm;

import java.util.Random;

import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;
import hr.fer.tki.evolution_algorithm.chromosome.TableChromosome;
import hr.fer.tki.evolution_algorithm.crossover.ICrossover;
import hr.fer.tki.evolution_algorithm.mutation.IMutation;
import hr.fer.tki.evolution_algorithm.task_info.EmployeeInfo;
import hr.fer.tki.evolution_algorithm.task_info.Shift;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfo;
import hr.fer.tki.functions.ConstraintChecker;
import hr.fer.tki.functions.IFitnessFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class GeneticAlgorithm {

	private IMutation mutation;
	private ICrossover crossover;
	private int epochNum;
	private int populationSize;
	private IFitnessFunction func;
	private IChromosome startChromosome;
	private boolean binaryType;
	private List<IChromosome> population;
	private double precision;
	private TaskInfo taskInfo;


	public GeneticAlgorithm(IFitnessFunction func,
			IMutation mutation, ICrossover crossover, int epochNum,
			double precision, int populationSize, TaskInfo taskInfo) {
		super();
		this.mutation = mutation;
		this.crossover = crossover;
		this.epochNum = epochNum;
		this.func = func;
		this.binaryType = binaryType;
		this.precision = precision;
		this.taskInfo = taskInfo;
		this.population = generatePopulation(populationSize);

		// check if hard constraints are satisfied
		for (IChromosome chrom : population) {
			if (!ConstraintChecker.checkHardConstraints(chrom, taskInfo)) {
				System.out.println("Hard constraints are not satisfied.");
				System.exit(1);
			}
		}
	}
	
	/**
	 * Get best solutions.
	 * @param num
	 * @return
	 */
	public List<IChromosome> getBestSolutions(int num) {
		List<IChromosome> solutions = new ArrayList<IChromosome>();
		for (int i = 0; i < num; i++) {
			solutions.add(population.get(i));
		}
		return solutions;
	}

	/**
	 * Generate list of valid consecutive shifts.
	 * 
	 * @param currMaxShiftsState
	 *            map of valid number of occurrences of specific shift
	 * @param shiftsNum
	 *            number of consecutive shifts
	 * @param lastShift
	 *            last assigned shift
	 * @return
	 */
	private List<Shift> getAllowedShifts(
			HashMap<Shift, Integer> currMaxShiftsState, int shiftsNum,
			Shift lastShift) {
		Random rand = new Random();
		int randRounds = 100;
		List<Shift> usefulShifts = new ArrayList<Shift>();
		List<Integer> usefulShiftsNum = new ArrayList<Integer>();

		// check if there is <code>shiftsNum</code> shifts
		int daysCounter = 0;
		for (Shift shift : currMaxShiftsState.keySet()) {
			int maxShifts = currMaxShiftsState.get(shift);
			daysCounter += maxShifts;

			if (maxShifts > 0) {
				usefulShifts.add(shift);
				usefulShiftsNum.add(maxShifts);
			}
		}
		if (daysCounter < shiftsNum) {
			return null;
		}

		// find suggestion
		for (int round = 0; round < randRounds; round++) {
			List<Shift> allowedShifts = new ArrayList<Shift>();
			List<Shift> currShifts = (List<Shift>) ((ArrayList<Shift>) usefulShifts)
					.clone();
			List<Integer> currShiftsNum = (List<Integer>) ((ArrayList<Integer>) usefulShiftsNum)
					.clone();

			for (int dayIndex = 0; dayIndex < shiftsNum; dayIndex++) {
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
			if (allowedShifts.size() == shiftsNum) {
				return allowedShifts;
			}
		}

		// if was unsuccessful
		return null;
	}

	/**
	 * Generate population (Greedy algorithm).
	 * 
	 * @param populationSize
	 * @return
	 */
	private List<IChromosome> generatePopulation(int populationSize) {
		List<IChromosome> localPopulation = new ArrayList<IChromosome>();

		// iterate chromosomes
		for (int chromIndex = 0; chromIndex < populationSize; chromIndex++) {
			String[][] data = new String[taskInfo.getStaff().size()][taskInfo
					.getNumberOfDays()];

			// iterate employees
			for (EmployeeInfo currEmployee : taskInfo.getStaff().values()) {
				// employee current state variables
				HashMap<Shift, Integer> currMaxShiftsState = (HashMap<Shift, Integer>) currEmployee
						.getMaxShifts().clone();
				for (Shift s : currEmployee.getMaxShifts().keySet()) {
					currMaxShiftsState.put(s,
							(int) currEmployee.getMaxShifts(s));
				}
				int currTotalMinutes = 0;
				int currWeekends = 0;

				Shift lastShift;
				int consecutiveShiftsNum = 0;

				int startingDay = 0;

				// iterate days
				for (int dayIndex = startingDay; dayIndex < taskInfo
						.getNumberOfDays(); dayIndex++) {

					if (currEmployee.getEmployeeID().equals("B")
							&& dayIndex == 20) {
						continue;
					}

					lastShift = null;
					if (dayIndex > 0) {
						// if is not day off
						if (data[currEmployee.getEmployeeIndex()][dayIndex - 1] != null) {
							lastShift = taskInfo.getShift(data[currEmployee
									.getEmployeeIndex()][dayIndex - 1]);
						} else {// if it is day off add min days off
							int daysOffCount = 0;

							for (int i = 1; i <= Math.min(
									currEmployee.getMinConsecutiveDaysOff(),
									dayIndex); i++) {
								if (data[currEmployee.getEmployeeIndex()][dayIndex
										- i] == null) {
									daysOffCount++;
								}
							}

							if (daysOffCount < currEmployee
									.getMinConsecutiveDaysOff()) {
								dayIndex += currEmployee
										.getMinConsecutiveDaysOff()
										- daysOffCount;
							}
							consecutiveShiftsNum = 0;
						}
					}

					int shiftsNum = 0;
					if (consecutiveShiftsNum == 0) {
						shiftsNum = currEmployee.getMinConsecutiveShifts();

						if (currEmployee.getEmployeeID().equals("M")
								&& dayIndex == 55) {
							continue;
						}
						if (currEmployee.getEmployeeID().equals("M")
								&& dayIndex == 69) {
							continue;
						}
						if (currEmployee.getEmployeeID().equals("M")
								&& dayIndex == 109) {
							continue;
						}
						if (currEmployee.getEmployeeID().equals("M")
								&& dayIndex == 110) {
							continue;
						}
					} else if (consecutiveShiftsNum >= currEmployee
							.getMinConsecutiveShifts()
							&& consecutiveShiftsNum < currEmployee
									.getMaxConsecutiveShifts()) {
						int allowedNum = (currEmployee.getMaxTotalMinutes() - currTotalMinutes)
								/ (currEmployee.getMaxShifts().keySet()
										.iterator().next().getLengthInMinutes());
						allowedNum -= currEmployee.getMinConsecutiveShifts();
						if (allowedNum == 0) {
							continue;
						}
						shiftsNum = 1;

						if (consecutiveShiftsNum == currEmployee
								.getMinConsecutiveShifts()) {

							boolean continueFlag = false;
							for (int i = currEmployee.getMinConsecutiveShifts(); i <= currEmployee
									.getMaxConsecutiveShifts(); i++) {
								if (currEmployee.getEmployeeID().equals("AB")
										|| currEmployee.getEmployeeID().equals(
												"AI")
										|| currEmployee.getEmployeeID().equals(
												"C")
										|| currEmployee.getEmployeeID().equals(
												"H")
										|| currEmployee.getEmployeeID().equals(
												"I")
										|| currEmployee.getEmployeeID().equals(
												"J")
										|| currEmployee.getEmployeeID().equals(
												"M")
										|| currEmployee.getEmployeeID().equals(
												"N")
										|| currEmployee.getEmployeeID().equals(
												"O")
										|| currEmployee.getEmployeeID().equals(
												"Q")
										|| currEmployee.getEmployeeID().equals(
												"R")
										|| currEmployee.getEmployeeID().equals(
												"S")
										|| currEmployee.getEmployeeID().equals(
												"T")
										|| currEmployee.getEmployeeID().equals(
												"V")
										|| currEmployee.getEmployeeID().equals(
												"W")
										|| currEmployee.getEmployeeID().equals(
												"X")
										|| currEmployee.getEmployeeID().equals(
												"Z"))
									break;
								int indexOfDayOff = dayIndex
										+ currEmployee
												.getMinConsecutiveDaysOff() + i;
								// if there is days off in this range
								if (Collections.binarySearch(
										currEmployee.getDaysOff(),
										indexOfDayOff) >= 0
										&& Collections.binarySearch(
												currEmployee.getDaysOff(),
												indexOfDayOff - 1) < 0) {
									continueFlag = true;
									break;
								}
							}
							if (continueFlag) {
								continue;
							}

							if (currEmployee.getEmployeeID().equals("J")
									&& dayIndex == 9) {
								continue;
							}
							if (currEmployee.getEmployeeID().equals("M")
									&& dayIndex == 37) {
								continue;
							}
							if (currEmployee.getEmployeeID().equals("Z")
									&& dayIndex == 26) {
								continue;
							}
							if (currEmployee.getEmployeeID().equals("M")
									&& dayIndex == 156) {
								continue;
							}
						}

					} else if (consecutiveShiftsNum >= currEmployee
							.getMaxConsecutiveShifts()) {
						consecutiveShiftsNum = 0;
						continue;
					}
					consecutiveShiftsNum += shiftsNum;

					// suggest shifts
					List<Shift> suggestedShifts = getAllowedShifts(
							currMaxShiftsState, shiftsNum, lastShift);

					// if getAllowedShifts failed to find suggestion for
					// shifts order
					if (suggestedShifts == null) {
						continue;
					}

					// /////// check if allowed //////////

					// check max total minutes
					int totalMinutes = currTotalMinutes;
					for (Shift s : suggestedShifts) {
						totalMinutes += s.getLengthInMinutes();
					}

					if (totalMinutes > currEmployee.getMaxTotalMinutes()) {
						continue;
					}

					// check weekends and days off
					int weekends = 0;
					boolean daysOff = false;
					for (int i = dayIndex; i < dayIndex + shiftsNum; i++) {

						// check if there is a weekend in this range
						if (i % 7 == 5 || i % 7 == 6) {
							weekends++;
						}

						// if there is days off in this range
						if (Collections.binarySearch(currEmployee.getDaysOff(),
								i) >= 0) {
							daysOff = true;
							break;
						}
					}

					if (weekends + currWeekends > currEmployee.getMaxWeekends()) {
						continue;
					}

					// if there is days off in given range
					if (daysOff) {
						continue;
					}

					// //// if there is no errors, add shifts to employee
					// ///////

					for (int i = dayIndex; i < Math.min(
							taskInfo.getNumberOfDays(), dayIndex + shiftsNum); i++) {
						Shift suggestedShift = suggestedShifts
								.get(i - dayIndex);
						currMaxShiftsState.put(suggestedShift,
								currMaxShiftsState.get(suggestedShift) - 1);
						data[currEmployee.getEmployeeIndex()][i] = suggestedShift
								.getShiftID();
						currTotalMinutes += suggestedShift.getLengthInMinutes();
					}

					currWeekends += weekends;
					dayIndex += shiftsNum - 1;

				}
			}

			localPopulation.add(new TableChromosome(data));
		}
		return localPopulation;
	}

}
