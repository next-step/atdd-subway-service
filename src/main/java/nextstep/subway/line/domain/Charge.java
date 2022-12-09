package nextstep.subway.line.domain;

import java.util.Set;

public class Charge {
    public static final int OVER_CHARGE = 100;
    public static final int DEFAULT_CHARGE = 1250;
    private int charge;
    public Charge(int distance) {
        this.charge = DEFAULT_CHARGE;
        distanceChargePolicy(distance);
    }

    public Charge(int distance, Set<Line> lines) {
        this.charge = DEFAULT_CHARGE;
        distanceChargePolicy(distance);
        lineCostPolicy(lines);
    }

    private void lineCostPolicy(Set<Line> lines) {
        int cost = 0;
        for (Line line : lines) {
            if(cost < line.getCost()){
                cost = line.getCost();
            }
        }
        this.charge += cost;
    }

    private void distanceChargePolicy(int distance) {
        if(distance > 50){
            int overDistance = distance - 50;
            this.charge += calculateOverFare(8, overDistance);
            distance -= overDistance;
        }
        if(distance > 10){
            int overDistance = distance - 10;
            this.charge += calculateOverFare(5, overDistance);
        }
    }

    private int calculateOverFare(int standard, int distance) {
        return (int) ((Math.ceil((distance - 1) / standard) + 1) * OVER_CHARGE);
    }

    public int value() {
        return this.charge;
    }
}
