package nextstep.subway.line.domain;

import java.util.Set;
import nextstep.subway.line.application.DistanceChargeCalculator;
import nextstep.subway.line.application.LineChargeCalculator;

public class Charge {

    public static final int DEFAULT_CHARGE = 1250;
    private int charge;

    public Charge(int distance, Set<Line> lines) {
        this.charge = LineChargeCalculator.calculate(DistanceChargeCalculator.calculate(DEFAULT_CHARGE, distance), lines);
    }

    public Charge(int distance, Set<Line> lines, int age) {
        this.charge = LineChargeCalculator.calculate(DistanceChargeCalculator.calculate(DEFAULT_CHARGE, distance), lines);
        agePolicy(age);
    }

    private void agePolicy(int age) {
        if(age >= 13 && age < 19){
            this.charge = (this.charge - 350) * 8 / 10;
        }
        if(age >= 6 && age < 13){
            this.charge = (this.charge - 350) * 5 / 10;
        }
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

    public int value() {
        return this.charge;
    }
}
