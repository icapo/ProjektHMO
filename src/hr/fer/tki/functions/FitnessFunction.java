package hr.fer.tki.functions;

import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;
import hr.fer.tki.evolution_algorithm.task_info.Cover;
import hr.fer.tki.evolution_algorithm.task_info.EmployeeInfo;
import hr.fer.tki.evolution_algorithm.task_info.ShiftRequest;
import hr.fer.tki.evolution_algorithm.task_info.TaskInfo;

import java.util.List;
import java.util.Map;

public class FitnessFunction implements IFitnessFunction {

    public double calculate(IChromosome chromosome, TaskInfo taskInfo) {
        double shiftsOnRequests = 0;
        double shiftsOffRequets = 0;
        double shiftCoverage = 0;

        // iterate employees
        for (EmployeeInfo currEmployee : taskInfo.getStaff().values()) {
            int rowIndex = currEmployee.getEmployeeIndex();
            String[] days = (String[]) chromosome.getChromosomeRow(rowIndex);

            shiftsOnRequests += this.calculateShiftsOnRequests(days, currEmployee);
            shiftsOffRequets += this.calculateShiftsOffRequests(days, currEmployee);
        }

        shiftCoverage += this.calculateShiftCoverage(chromosome, taskInfo);

        return shiftsOnRequests + shiftsOffRequets + shiftCoverage;

    }

    private double calculateShiftCoverage(IChromosome chromosome, TaskInfo taskInfo) {
        double totalError = 0;
        int days = taskInfo.getNumberOfDays();
        Map<Integer, List<Cover>> coversMap = taskInfo.getCover();

        for (int i = 0; i < days; i++) {
            String[] dayShifts = (String[]) chromosome.getChromosomeColumn(i);
            List<Cover> covers = coversMap.get(i);

            for(Cover cover : covers) {

                int requirement = cover.getRequirement();
                int shiftsNumber = 0;

                for(String shift : dayShifts) {
                    if(shift == null) {
                        continue;
                    }
                    if(shift.equals(cover.getShiftID())) {
                        shiftsNumber++;
                    }
                }
                if(requirement < shiftsNumber) {
                    totalError += ((shiftsNumber - requirement) * cover.getWeightForOver());
                }else if(requirement > shiftsNumber) {
                    totalError += ((requirement - shiftsNumber) * cover.getWeightForUnder());
                }
            }
        }

        return totalError;
    }

    private double calculateShiftsOnRequests(String[] days, EmployeeInfo currEmployee) {
        int totalError = 0;
        for (int i = 0; i < days.length; i++) {
            String shiftId = days[i];
            for (ShiftRequest shiftRequest : currEmployee.getShiftOnRequests()) {
                if (shiftRequest.getDayIndex() != i) {
                    continue;
                }
                if (!shiftRequest.getShiftID().equals(shiftId)) {
                    totalError += shiftRequest.getWeight();
                    break;
                }
            }
        }

        return totalError;
    }

    private double calculateShiftsOffRequests(String[] days, EmployeeInfo currEmployee) {
        int totalError = 0;
        for (int i = 0; i < days.length; i++) {
            String shiftId = days[i];
            for (ShiftRequest shiftRequest : currEmployee.getShiftOffRequests()) {
                if (shiftRequest.getDayIndex() != i) {
                    continue;
                }
                if (shiftRequest.getShiftID().equals(shiftId)) {
                    totalError += shiftRequest.getWeight();
                    break;
                }
            }
        }

        return totalError;
    }
}
