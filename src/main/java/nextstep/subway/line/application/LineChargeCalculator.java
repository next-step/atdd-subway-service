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
            lineCost = getGreaterCost(lineCost, line);
        }
        return lineCost;
    }

    private static int getGreaterCost(int lineCost, Line line) {
        if(lineCost < line.getCost()){
            return line.getCost();
        }
        return lineCost;
    }
}
