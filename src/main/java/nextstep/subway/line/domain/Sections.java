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
    private final List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }
        Collections.sort(sections);
        List<Station> stations = new ArrayList<>();
        stations.add(findUpStation());
        stations.addAll(sections.stream()
                .map(s -> s.getDownStation())
                .collect(Collectors.toList()));
        return stations;
    }

    private Station findUpStation() {
        return sections.get(0).getUpStation();
    }

    public void addStation(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();
        if (stations.isEmpty()) {
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }

        validateAddStation(stations, upStation, downStation);

        if (isUpStationExisted(stations, upStation)) {
            sections.stream()
                    .filter(it -> it.getUpStation() == upStation)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, distance));

            sections.add(new Section(line, upStation, downStation, distance));
        } else if (isDownStationExisted(stations, downStation)) {
            sections.stream()
                    .filter(it -> it.getDownStation() == downStation)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, distance));

            sections.add(new Section(line, upStation, downStation, distance));
        }
    }

    private void validateAddStation(List<Station> stations, Station upStation, Station downStation) {
        boolean isUpStationExisted = isUpStationExisted(stations, upStation);
        boolean isDownStationExisted = isDownStationExisted(stations, downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isDownStationExisted(List<Station> stations, Station downStation) {
        return stations.stream()
                .anyMatch(it -> it == downStation);
    }

    private boolean isUpStationExisted(List<Station> stations, Station upStation) {
        return stations.stream()
                .anyMatch(it -> it == upStation);
    }

    public void removeLineStation(Line line, Station station) {
        validateRemoveLineStation();

        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void validateRemoveLineStation() {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }
    }
}
