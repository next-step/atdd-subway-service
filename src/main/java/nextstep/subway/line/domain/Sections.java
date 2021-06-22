package nextstep.subway.line.domain;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@Embeddable
public class Sections {

    public static final int SECTION_DELETABLE_MIN_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public List<StationResponse> stationResponse() {
        return getStations().stream()
                .map(Station::toResponse)
                .collect(Collectors.toList());
    }

    public int size() {
        return sections.size();
    }

    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = isExistStation(upStation);
        boolean isDownStationExisted = isExistStation(downStation);

        checkValidStations(stations, isUpStationExisted, isDownStationExisted);

        if (stations.isEmpty()) {
            sections.add(createSection(line, upStation, downStation, distance));
            return;
        }
        if (isUpStationExisted) {
            findSectionByUpStation(upStation).ifPresent(section -> section.updateUpStation(downStation, distance));
            sections.add(createSection(line, upStation, downStation, distance));
            return;
        }
        if (isDownStationExisted) {
            findSectionByDownStation(downStation).ifPresent(section -> section.updateDownStation(upStation, distance));
            sections.add(createSection(line, upStation, downStation, distance));
            return;
        }
        throw new RuntimeException();
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station firstStation = findFirstStation();
        stations.add(firstStation);

        while (hasSectionByUpStation(firstStation)) {
            Station tempStation = firstStation;
            Section nextLineSection = findSectionByUpStation(tempStation)
                    .orElseThrow(() -> new RuntimeException("구간을 찾을 수 없습니다."));
            firstStation = nextLineSection.getDownStation();
            stations.add(firstStation);
        }
        return stations;
    }

    public void removeStation(Line line, Station station) {
        checkDeletable();

        Optional<Section> upSection = findSectionByUpStation(station);
        Optional<Section> downSection = findSectionByDownStation(station);

        if (upSection.isPresent() && downSection.isPresent()) {
            mergeSection(line, upSection.get(), downSection.get());
        }

        upSection.ifPresent(it -> sections.remove(it));
        downSection.ifPresent(it -> sections.remove(it));
    }

    private void mergeSection(Line line, Section upSection, Section downSection) {
        Station newUpStation = downSection.getUpStation();
        Station newDownStation = upSection.getDownStation();
        int newDistance = upSection.getDistance() + downSection.getDistance();
        sections.add(createSection(line, newUpStation, newDownStation, newDistance));
    }

    private Station findFirstStation() {
        Station firstStation = sections.get(0).getUpStation();
        while (hasSectionByDownStation(firstStation)) {
            Station tempStation = firstStation;
            Section nextLineSection = findSectionByDownStation(tempStation)
                    .orElseThrow(() -> new RuntimeException("구간을 찾을 수 없습니다."));
            firstStation = nextLineSection.getUpStation();
        }
        return firstStation;
    }

    private void checkValidStations(List<Station> stations, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private Section createSection(Line line, Station upStation, Station downStation, int distance) {
        return new Section(line, upStation, downStation, distance);
    }

    private boolean hasSectionByDownStation(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isDownStation(station));
    }

    private boolean hasSectionByUpStation(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isUpStation(station));
    }

    private Optional<Section> findSectionByDownStation(Station station) {
        return sections.stream()
                .filter(section -> section.isDownStation(station))
                .findFirst();
    }

    private Optional<Section> findSectionByUpStation(Station station) {
        return sections.stream()
                .filter(section -> section.isUpStation(station))
                .findFirst();
    }

    private void checkDeletable() {
        if (sections.size() <= SECTION_DELETABLE_MIN_SIZE) {
            throw new RuntimeException("구간이 1개밖에 없습니다.");
        }
    }

    private boolean isExistStation(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isExistsStation(station));
    }
}
