package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Stations;

import java.util.ArrayList;
import java.util.List;

public class Lines {
    private List<Line> values = new ArrayList<>();

    protected Lines() {
    }

    public Lines(List<Line> values) {
        this.values = values;
    }

    public boolean containsStationsExactly(Stations stations) {

        return values.stream()
                .anyMatch(line -> line.containsStationsExactly(stations));
    }

    @Override
    public String toString() {
        return "Lines{" +
                "values=" + values +
                '}';
    }
}
