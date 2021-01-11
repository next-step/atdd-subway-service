package nextstep.subway.line.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.Money;
import nextstep.subway.station.domain.Station;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-06
 */
public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = Collections.unmodifiableList(lines);
    }

    public List<Station> getStations() {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return lines.stream()
                .flatMap(line -> line.getSections().stream())
                .collect(Collectors.toList());
    }

    public boolean contains(Station station) {
        return getStations().contains(station);
    }

    public Money findMaxAdditionalFare(List<Station> stations) {
        if (stations.isEmpty()) {
            throw new IllegalArgumentException();
        }
        List<Section> sections = getSections();
        Money maxAdditionalFare = Money.ZERO;

        Station preStation = stations.get(0);
        for (int i = 1; i < stations.size(); i++) {
            Line line = findLineByUpStationAndDownStation(sections, preStation, stations.get(i));
            maxAdditionalFare = Money.max(maxAdditionalFare, line.getAdditionalFare());
            preStation = stations.get(i);
        }
        return maxAdditionalFare;
    }

    private Line findLineByUpStationAndDownStation(List<Section> sections, Station upStation, Station downStation) {
        Section section = sections.stream()
                .filter(it -> upStation.equals(it.getUpStation()) && downStation.equals(it.getDownStation()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        return section.getLine();
    }

}
