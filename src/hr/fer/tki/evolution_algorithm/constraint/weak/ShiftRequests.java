package hr.fer.tki.evolution_algorithm.constraint.weak;


public class ShiftRequests {
    private int counter =0 ;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void increment() {
        this.counter++;
    }
}
