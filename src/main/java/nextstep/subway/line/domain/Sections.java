package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return getStations(new ArrayList());
    }

    public List<Station> getStations(List<Station> stations) {
        Map<Station, Station> upToDowns = getUpToDownStation();
        Station upStation = getUpStation();

        while (upStation != null) {
            stations.add(upStation);
            upStation = upToDowns.get(upStation);
        }

        return stations;
    }

    private Map<Station, Station> getUpToDownStation() {
        return sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
    }

    private Station getUpStation() {
        List<Station> downStations = sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        return sections.stream()
                .map(Section::getUpStation)
                .filter(upStation -> !downStations.contains(upStation))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public void updateUpStation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    public void updateDownStation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    public boolean hasStation(Station station) {
        return getStations().stream()
                .anyMatch(it -> it == station);
    }
}
