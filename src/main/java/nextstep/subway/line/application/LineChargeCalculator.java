package nextstep.subway.line.application;

import java.util.Set;
import nextstep.subway.line.domain.Line;

public class LineChargeCalculator {
    public static int calculate(int charge, Set<Line> lines) {
        return charge + getMaxLineCost(lines);
    }

    private static int getMaxLineCost(Set<Line> lines) {
        int lineCost = 0;
        for (Line line : lines) {
            if(lineCost < line.getCost()){
                lineCost = line.getCost();
            }
        }
        return lineCost;
    }
}
