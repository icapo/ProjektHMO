package hr.fer.tki.functions;


import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;
import hr.fer.tki.evolution_algorithm.task_info.EmployeeInfo;
import hr.fer.tki.evolution_algorithm.task_info.Shift;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfo;

import java.util.Collections;
import java.util.HashMap;

public class ConstraintChecker {

    /**
     * Check hard constraints
     *
     * @param chromosome    chromosome
     * @param taskInfo task informations
     * @return <code>true</code> if all constraints are satisfied,
     * <code>false</code> if not
     */
    public static boolean checkHardConstraints(IChromosome chromosome, TaskInfo taskInfo) {
        // iterate employees
        for (EmployeeInfo currEmployee : taskInfo.getStaff().values()) {

            int rowIndex = currEmployee.getEmployeeIndex();
            // employee current state variables
            HashMap<Shift, Integer> currMaxShiftsState = (HashMap<Shift, Integer>) currEmployee.getMaxShifts().clone();
            int totalMinutes = 0;
            int weekendsCounter = 0;
            int consecutiveShifts = 0;
            int consecutiveDaysOff = 0;

            // iterate days
            for (int colIndex = 1; colIndex < chromosome.getColsNum(); colIndex++) {

                String prevShiftID = (String) chromosome.getChromosomeElement(
                        rowIndex, colIndex - 1);
                String currShiftID = (String) chromosome.getChromosomeElement(
                        rowIndex, colIndex);

                Shift prevShift = taskInfo.getShift(prevShiftID);
                Shift currShift = taskInfo.getShift(currShiftID);

                // update number of occurrences of specific shift
                if (colIndex == 1 && prevShift != null) {
                    currMaxShiftsState.put(prevShift,
                            currMaxShiftsState.get(prevShift) - 1);
                }
                if (currShift != null) {
                    currMaxShiftsState.put(currShift,
                            currMaxShiftsState.get(currShift) - 1);
                }

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
                        weekendsCounter++;
                    }
                }

                // update consecutiveDaysOff
                if (colIndex == 1 && prevShift == null) {
                    consecutiveDaysOff += currEmployee.getMinConsecutiveDaysOff();
                }
                if (currShift == null) {
                    consecutiveDaysOff++;
                } else if (prevShift == null) {
                    if (consecutiveDaysOff < currEmployee.getMinConsecutiveDaysOff()) {
                        return false;
                    }
                    consecutiveDaysOff = 0;
                }

                // update consecutiveShifts
                if (colIndex == 1 && prevShift != null) {
                    consecutiveShifts++;
                }
                if (currShift != null) {
                    consecutiveShifts++;
                } else if (prevShift != null) {
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
                    if (Collections.binarySearch(currEmployee.getDaysOff(), 0) >= 0) {
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
            if (totalMinutes > currEmployee.getMaxTotalMinutes()
                    || totalMinutes < currEmployee.getMinTotalMinutes()) {
                return false;
            }

            // check weekends
            if (weekendsCounter > currEmployee.getMaxWeekends()) {
                return false;
            }
            // check number of occurrences of specific shift
            for (Integer num : currMaxShiftsState.values()) {
                if (num < 0) {
                    return false;
                }
            }
        }
        // if all chromosomes valid
        return true;
    }

}
