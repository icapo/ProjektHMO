package hr.fer.tki.evolution_algorithm.constraint;


import hr.fer.tki.evolution_algorithm.constraint.hard.*;
import hr.fer.tki.evolution_algorithm.constraint.weak.ShiftRequestsConstraint;
import hr.fer.tki.evolution_algorithm.constraint.weak.ShiftsCover;
import hr.fer.tki.evolution_algorithm.task_info.ShiftRequest;

public class ConstraintContainer {

    public ConsecutiveDaysOff consecutiveDaysOff;

    public DaysOff daysOff;

    public ShiftRotation shiftRotation;

    public ShiftsInterval shiftsInterval;

    public SpecificShifts specificShifts;

    public TotalMinutes totalMinutes;

    public WeekendCounter weekendCounter;

    public ShiftRequestsConstraint shiftRequest;

    public ShiftsCover shiftsCover;

    public ConstraintContainer() {
        this.consecutiveDaysOff = new ConsecutiveDaysOff();
        this.daysOff = new DaysOff();
        this.shiftRotation = new ShiftRotation();
        this.shiftsInterval = new ShiftsInterval();
        this.specificShifts = new SpecificShifts();
        this.totalMinutes = new TotalMinutes();
        this.weekendCounter = new WeekendCounter();
        this.shiftRequest = new ShiftRequestsConstraint();
        this.shiftsCover = new ShiftsCover();

    }

    public boolean isValidSolution() {
        return this.consecutiveDaysOff.getCounter() == 0 &&
                this.daysOff.getCounter() == 0 &&
                this.shiftRotation.getCounter() == 0 &&
                this.shiftsInterval.getCounter() == 0 &&
                this.specificShifts.getCounter() == 0 &&
                this.totalMinutes.getCounter() == 0 &&
                this.weekendCounter.getCounter() == 0 &&
                this.shiftRequest.getCounter() == 0 &&
                this.shiftsCover.getCounter() == 0;
    }
}
