package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;

public class Lines {

    private List<Line> lines = new ArrayList<>();

    public void addAll(List<Line> lines) {
        this.lines = lines;
    }

    public int addFare() {
        int addFare = lines.get(0).getFare();
        for (Line line : lines) {
            if (line.getFare() > addFare) {
                addFare = line.getFare();
            }
        }
        return addFare;
    }
}
