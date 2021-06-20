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

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();
        validateStations(stations, upStation, downStation);

        if (stations.contains(upStation)) {
            getSectionByUpStation(upStation)
                    .ifPresent(v -> v.updateUpStation(downStation, distance));
        }
        if (stations.contains(downStation)) {
            getSectionByDownStation(downStation)
                    .ifPresent(it -> it.updateDownStation(upStation, distance));
        }
        sections.add(Section.of(line, upStation, downStation, distance));
    }

    private Optional<Section> getSectionByUpStation(Station upStation) {
        return sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst();
    }

    private Optional<Section> getSectionByDownStation(Station downStation) {
        return sections.stream()
                .filter(it -> it.getUpStation() == downStation)
                .findFirst();
    }

    private void validateStations(List<Station> stations, Station upStation, Station downStation) {
        if (stations.size() > 0 && stations.contains(upStation) && stations.contains(downStation)) {
            throw new IllegalStateException(LINE_DUPLICATED);
        }
        if (stations.size() > 0 && !stations.contains(upStation) && !stations.contains(downStation)) {
            throw new IllegalStateException(NOT_VALID_STATION_EXISTED);
        }
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
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
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
}
