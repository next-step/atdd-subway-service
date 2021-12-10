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
        stations.add(findFirstStation());
        stations.addAll(sections.stream()
                .map(s -> s.getDownStation())
                .collect(Collectors.toList()));
        return stations;
    }

    private Station findFirstStation() {
        return sections.get(0).getUpStation();
    }

    public void addStation(Line line, Station upStation, Station downStation, int distance) {
        if (getStations().isEmpty()) {
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }

        validateAddStation(upStation, downStation);
        findConnectedSection(upStation, downStation).ifPresent(s -> s.updateStation(upStation, downStation, distance));
        sections.add(new Section(line, upStation, downStation, distance));
    }

    private Optional<Section> findConnectedSection(Station upStation, Station downStation) {
        Optional<Section> findUpStation = getUpStation(upStation);
        if (findUpStation.isPresent()) {
            return findUpStation;
        }

        return getDownStation(downStation);
    }

    private void validateAddStation(Station upStation, Station downStation) {
        boolean isUpStationExisted = isUpStationExisted(upStation);
        boolean isDownStationExisted = isDownStationExisted(downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }

        if (!getStations().isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isDownStationExisted(Station downStation) {
        return getStations().stream()
                .anyMatch(it -> it.equals(downStation));
    }

    private boolean isUpStationExisted(Station upStation) {
        return getStations().stream()
                .anyMatch(it -> it.equals(upStation));
    }

    public void removeLineStation(Line line, Station station) {
        validateRemoveLineStation();

        Optional<Section> upLineStation = getUpStation(station);
        Optional<Section> downLineStation = getDownStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private Optional<Section> getUpStation(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation().equals(station))
                .findFirst();
    }

    private Optional<Section> getDownStation(Station downStation) {
        return sections.stream()
                .filter(it -> it.getDownStation().equals(downStation))
                .findFirst();
    }

    private void validateRemoveLineStation() {
        if (sections.size() <= 1) {
            throw new IllegalArgumentException("구간이 1개만 남아있어 삭제할 수 없습니다.");
        }
    }

    public List<Section> getSections() {
        return this.sections;
    }

    public void addStation(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();
        validateAddStation(stations, upStation, downStation);

        if (stations.isEmpty()) {
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }

        if (isUpStationExisted(stations, upStation)) {
            sections.stream()
                    .filter(it -> it.getUpStation() == upStation)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, distance));

            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }

        if (isDownStationExisted(stations, downStation)) {
            sections.stream()
                    .filter(it -> it.getDownStation() == downStation)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, distance));

            sections.add(new Section(line, upStation, downStation, distance));
            return;
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
}
