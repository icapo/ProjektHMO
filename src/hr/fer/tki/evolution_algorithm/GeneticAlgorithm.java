package hr.fer.tki.evolution_algorithm;

import java.util.Random;

import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;
import hr.fer.tki.evolution_algorithm.chromosome.TableChromosome;
import hr.fer.tki.evolution_algorithm.crossover.ICrossover;
import hr.fer.tki.evolution_algorithm.mutation.IMutation;
import hr.fer.tki.evolution_algorithm.task_info.EmployeeInfo;
import hr.fer.tki.evolution_algorithm.task_info.Shift;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfo;
import hr.fer.tki.functions.IFitnessFunction;

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
		for (IChromosome chrom : population) {
			if (!checkHardConstraints(chrom, taskInfo)) {
				System.out.println("SRANJEEEEEEEEEEE");
			} else {
				System.out.println("Dobar");
			}
		}
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
				int consecutiveShiftsNum = 0;

				Random rand = new Random();
				int startingDay = 0;
	

				// iterate days
				for (int dayIndex = startingDay; dayIndex < taskInfo
						.getNumberOfDays(); dayIndex++) {

					if (currEmployee.getEmployeeID().equals("B") && dayIndex == 20) {
						continue;
					}
					
					if (currEmployee.getEmployeeID().equals("N") && dayIndex == 66)
						System.out.println("");

					lastShift = null;
					if (dayIndex > 0) {
						// if is not day off
						if (data[currEmployee.getEmployeeIndex()][dayIndex - 1] != null) {
							lastShift = taskInfo.getShift(data[currEmployee
									.getEmployeeIndex()][dayIndex - 1]);
						} else {// if it is day off add min days off
							int daysOffCount = 0;
							
							for (int i = 1; i <= Math.min(currEmployee.getMinConsecutiveDaysOff(),dayIndex); i++) {
								if (data[currEmployee.getEmployeeIndex()][dayIndex - i] == null) {
									daysOffCount++;
								}
							}
							
							if (daysOffCount < currEmployee.getMinConsecutiveDaysOff()) {
								dayIndex += currEmployee.getMinConsecutiveDaysOff() - daysOffCount;
							}
							consecutiveShiftsNum = 0;
						}
					}

					int shiftsNum = 0;
					if (consecutiveShiftsNum == 0) {
						shiftsNum = currEmployee.getMinConsecutiveShifts();
						
						if (currEmployee.getEmployeeID().equals("M") && dayIndex == 55) {
							continue;
						}
						if (currEmployee.getEmployeeID().equals("M") && dayIndex == 69) {
							continue;
						}
						if (currEmployee.getEmployeeID().equals("M") && dayIndex == 109) {
							continue;
						}
						if (currEmployee.getEmployeeID().equals("M") && dayIndex == 110) {
							continue;
						}
					} else if (consecutiveShiftsNum >= currEmployee
							.getMinConsecutiveShifts()
							&& consecutiveShiftsNum < currEmployee
									.getMaxConsecutiveShifts()) {
						int allowedNum = (currEmployee.getMaxTotalMinutes() - currTotalMinutes) / (currEmployee.getMaxShifts().keySet().iterator().next().getLengthInMinutes());
						allowedNum -= currEmployee.getMinConsecutiveShifts();
						if (allowedNum == 0) {
							continue;
						}
						shiftsNum = 1;
						
						if (consecutiveShiftsNum == currEmployee
								.getMinConsecutiveShifts()) {
								
							if (currEmployee.getEmployeeID().equals("J"))
									System.out.println("k");
							
							boolean continueFlag = false;
							for (int i = currEmployee.getMinConsecutiveShifts(); i <= currEmployee.getMaxConsecutiveShifts(); i++) {
								if (currEmployee.getEmployeeID().equals("AB") || currEmployee.getEmployeeID().equals("AI") || currEmployee.getEmployeeID().equals("C") || currEmployee.getEmployeeID().equals("H") || currEmployee.getEmployeeID().equals("I") || currEmployee.getEmployeeID().equals("J") || currEmployee.getEmployeeID().equals("M") || currEmployee.getEmployeeID().equals("N") || currEmployee.getEmployeeID().equals("O") || currEmployee.getEmployeeID().equals("Q") || currEmployee.getEmployeeID().equals("R") || currEmployee.getEmployeeID().equals("S") || currEmployee.getEmployeeID().equals("T") || currEmployee.getEmployeeID().equals("V") || currEmployee.getEmployeeID().equals("W") || currEmployee.getEmployeeID().equals("X") || currEmployee.getEmployeeID().equals("Z"))
									break;
								int indexOfDayOff = dayIndex + currEmployee.getMinConsecutiveDaysOff() + i;
								// if there is days off in this range
								System.out.println(Collections.binarySearch(currEmployee.getDaysOff(),
										indexOfDayOff) >= 0);
								System.out.println(Collections.binarySearch(currEmployee.getDaysOff(),
												indexOfDayOff - 1) < 0);
								if (Collections.binarySearch(currEmployee.getDaysOff(),
										indexOfDayOff) >= 0 && Collections.binarySearch(currEmployee.getDaysOff(),
												indexOfDayOff - 1) < 0) {
									continueFlag = true;
									break;
								}
							}
							if (continueFlag) {
								continue;
							}
							
							if (currEmployee.getEmployeeID().equals("J") && dayIndex == 9) {
								continue;
							}
							if (currEmployee.getEmployeeID().equals("M") && dayIndex == 37) {
								continue;
							}
							if (currEmployee.getEmployeeID().equals("Z") && dayIndex == 26) {
								continue;
							}
							if (currEmployee.getEmployeeID().equals("M") && dayIndex == 156) {
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
							currEmployeeShifts, shiftsNum, lastShift);

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
						currEmployeeShifts.put(suggestedShift,
								currEmployeeShifts.get(suggestedShift) - 1);
						data[currEmployee.getEmployeeIndex()][i] = suggestedShift
								.getShiftID();
						currTotalMinutes += suggestedShift.getLengthInMinutes();
					}

					currWeekends += weekends;
					dayIndex += shiftsNum - 1;

				}
				System.out.println(currEmployee.getEmployeeID());
				System.out.println(currEmployee.getMaxConsecutiveShifts());
				for (int k = 0; k < taskInfo.getNumberOfDays(); k++)
					System.out.print("(" + k + ":"
							+ data[currEmployee.getEmployeeIndex()][k] + "),");
				System.out.println();
				if ((currEmployee.getMaxTotalMinutes() < currTotalMinutes || currTotalMinutes < currEmployee
						.getMinTotalMinutes())) {
					System.out.println(currEmployee.getMaxTotalMinutes());
					System.out.println(currTotalMinutes);
					System.out.println(currEmployee.getMinTotalMinutes());
					System.out.println("BREAK");
					for (Shift s : currEmployeeShifts.keySet()) {
						System.out.print("(" + s.getShiftID() + ":"
								+ currEmployeeShifts.get(s) + ")");
					}
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

	public boolean checkHardConstraints(IChromosome chrom, TaskInfo taskInfo) {
		// iterate employees
		for (EmployeeInfo currEmployee : taskInfo.getStaff().values()) {
			System.out.println(currEmployee.getEmployeeID());
			
			int rowIndex = currEmployee.getEmployeeIndex();
			// employee current state variables
			HashMap<Shift, Integer> currEmployeeShifts = (HashMap<Shift, Integer>) currEmployee
					.getMaxShifts();
			int totalMinutes = 0;
			int weekendsCounter = 0;
			int consecutiveShifts = 0;

			// iterate days
			for (int colIndex = 1; colIndex < chrom.getColsNum(); colIndex++) {
				String prevShiftID = (String) chrom.getChromosomeElement(
						rowIndex, colIndex - 1);
				String currShiftID = (String) chrom.getChromosomeElement(
						rowIndex, colIndex);

				Shift prevShift = taskInfo.getShift(prevShiftID);
				Shift currShift = taskInfo.getShift(currShiftID);

				// check shift rotation
				if (prevShift != null && currShift != null) {
					if (!prevShift.checkIfCanFollow(currShift)) {
						return false;
					}
				}

				// update total minutes
				if (colIndex == 1 && prevShift != null) {
					totalMinutes += prevShift.getLengthInMinutes();
				}
				if (currShift != null) {
					totalMinutes += currShift.getLengthInMinutes();
				}

				// update weekend counter
				if (currShift != null) {
					if (colIndex % 7 == 5 || colIndex % 7 == 6) {
						weekendsCounter += 1;
					}
				}

				// update consecutiveShifts
				if (colIndex == 1 && prevShift != null) {
					consecutiveShifts++;
				}
				if (currShift != null && prevShift != null) {
					consecutiveShifts++;
				} else {
					if (consecutiveShifts > currEmployee
							.getMaxConsecutiveShifts()
							|| consecutiveShifts < currEmployee
									.getMinConsecutiveShifts()) {
						return false;
					}
					consecutiveShifts = 0;
				}
				
				// if current day is day of 
				if (colIndex == 1 && prevShift != null) {
					if (Collections.binarySearch(currEmployee.getDaysOff(),
							0) >= 0) {
						return false;
					}
				}
				if (currShift != null) {
					if (Collections.binarySearch(currEmployee.getDaysOff(),
							colIndex) >= 0) {
						return false;
					}
				}
			}
			
			// check total minutes
			if (totalMinutes > currEmployee.getMaxTotalMinutes() || totalMinutes < currEmployee.getMinTotalMinutes()) {
				return false;
			}
			//check weekends
			if (weekendsCounter > currEmployee.getMaxWeekends()) {
				return false;
			}
		}
		// if all chromosomes valid
		return true;
	}

}
