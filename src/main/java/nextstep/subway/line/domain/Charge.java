package nextstep.subway.line.domain;

import java.util.Set;
import nextstep.subway.line.application.AgeChargeCalculator;
import nextstep.subway.line.application.DistanceChargeCalculator;
import nextstep.subway.line.application.LineChargeCalculator;

public class Charge {

    public static final int DEFAULT_CHARGE = 1250;
    private int charge;

    public Charge(int distance, Set<Line> lines) {
        this.charge = chargeLineCost(chargeDistance(DEFAULT_CHARGE, distance), lines);
    }

    public Charge(int distance, Set<Line> lines, int age) {
        this(distance, lines);
        this.charge = discountAge(this.charge, age);
    }

    private int chargeDistance(int charge, int distance){
        return DistanceChargeCalculator.calculate(charge, distance);
    }

    private int chargeLineCost(int charge, Set<Line> lines){
        return LineChargeCalculator.calculate(charge, lines);
    }

    private int discountAge(int charge, int age){
        return AgeChargeCalculator.calculate(charge, age);
    }

    public int value() {
        return this.charge;
    }

    public void discount(int age) {
        this.charge = discountAge(this.charge, age);
    }
}
