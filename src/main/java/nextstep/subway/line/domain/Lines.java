package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public List<Station> getStations() {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return lines.stream()
                .flatMap(line -> line.getSections().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public boolean hasNotStation(Station station) {
        return lines.stream()
                .noneMatch(line -> line.getStations().contains(station));
    }

    public int maxExtraFare() {
        Fare maxFare = lines.stream()
                .map(Line::getExtraFare)
                .max(Comparator.comparingInt(Fare::value))
                .orElse(new Fare(Fare.FREE));
        return maxFare.value();
    }
}
