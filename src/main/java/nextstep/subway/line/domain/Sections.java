package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    private static final String LINE_DUPLICATED = "노선이 중복되었습니다.";
    private static final String NOT_VALID_STATION_EXISTED = "역이 포함되지 않아, 등록할 수 없습니다.";
    private static final int MINIMUM_REMOVAL_LIMIT = 1;
    private static final String REMOVAL_NOT_ALLOWED = "삭제할 수 없습니다";

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findUpToDownConnected(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }
        return stations;
    }

    private Optional<Section> findUpToDownConnected(Station finalDownStation) {
        return sections.stream()
                .filter(it -> it.getUpStation() == finalDownStation)
                .findFirst();
    }

    private Station findUpStation() {
        Station downStation = sections.stream()
                .findFirst()
                .map(Section::getUpStation)
                .orElse(null);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }
        return downStation;
    }

    public void removeSection(Line line, Station targetStation) {
        if (sections.size() <= MINIMUM_REMOVAL_LIMIT) {
            throw new IllegalStateException(REMOVAL_NOT_ALLOWED);
        }
        Optional<Section> upwardSection = findSectionWithUpStation(targetStation);
        Optional<Section> downwardSection = findSectionWithDownStation(targetStation);

        if (upwardSection.isPresent() && downwardSection.isPresent()) {
            Station newUpStation = downwardSection.get().getUpStation();
            Station newDownStation = upwardSection.get().getDownStation();
            int newDistance = upwardSection.get().getDistance() + downwardSection.get().getDistance();
            sections.add(Section.of(line, newUpStation, newDownStation, newDistance));
        }

        upwardSection.ifPresent(sections::remove);
        downwardSection.ifPresent(sections::remove);
    }

    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();
        validateStations(stations, upStation, downStation);

        if (stations.contains(upStation)) {
            findSectionWithUpStation(upStation)
                    .ifPresent(s -> s.updateUpStation(downStation, distance));
        }
        if (stations.contains(downStation)) {
            findSectionWithDownStation(downStation)
                    .ifPresent(s -> s.updateDownStation(upStation, distance));
        }
        sections.add(Section.of(line, upStation, downStation, distance));
    }

    private void validateStations(List<Station> stations, Station upStation, Station downStation) {
        if (stations.size() > 0 && stations.contains(upStation) && stations.contains(downStation)) {
            throw new IllegalStateException(LINE_DUPLICATED);
        }
        if (stations.size() > 0 && !stations.contains(upStation) && !stations.contains(downStation)) {
            throw new IllegalStateException(NOT_VALID_STATION_EXISTED);
        }
    }

    private Optional<Section> findSectionWithUpStation(Station upStation) {
        return sections.stream()
                .filter(s -> s.getUpStation() == upStation)
                .findAny();
    }

    private Optional<Section> findSectionWithDownStation(Station downStation) {
        return sections.stream()
                .filter(s -> s.getDownStation() == downStation)
                .findAny();
    }
}
