package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void addLineStation(Section section) {
        sections.add(section);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        return getStationsInOrder();
    }

    private List<Station> getStationsInOrder() {
        List<Station> stations = new ArrayList<>();
        Station station = findUpFinalStation();

        while (station != null) {
            stations.add(station);
            station = findNextStation(station);
        }
        return stations;
    }

    private Station findUpFinalStation() {
        List<Station> upStations = sections.stream()
                .map(Section::getUpStation).collect(Collectors.toList());
        List<Station> downStations = sections.stream()
                .map(Section::getDownStation).collect(Collectors.toList());
        upStations.removeAll(downStations);

        if (upStations.size() != 1) {
            throw new NoSuchElementException("상행종점역을 찾을 수 없습니다.");
        }
        return upStations.get(0);
    }

    private Station findNextStation(Station station) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(station))
                .findFirst()
                .orElse(new Section())
                .getDownStation();
    }
}
