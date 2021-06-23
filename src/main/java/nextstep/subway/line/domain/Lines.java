package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public Lines(Line... lines) {
        this.lines = new ArrayList<>(Arrays.asList(lines));
    }

    public List<Section> getSections() {
        return lines.stream()
            .flatMap(line -> line.getSections().getSections().stream())
            .collect(Collectors.toList());
    }

    public List<Station> getStations() {
        return lines.stream()
            .flatMap(line -> line.getSections().findStationsInOrder().stream())
            .collect(Collectors.toList());
    }
}
