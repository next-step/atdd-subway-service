package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {
    public static final String EXISTS_SECTION_EXCEPTION_MESSAGE = "이미 등록된 구간 입니다.";
    public static final String NOT_EXISTS_ALL_STATIONS_EXCEPTION_MESSAGE = "등록할 수 없는 구간 입니다.";
    public static final String AT_LEAST_ONE_SECTION_EXCEPTION_MESSAGE = "구간은 최소 한개 이상이어야만 합니다.";

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections;

    protected Sections() {
        sections = new ArrayList<>();
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findFirstUpStation();
        stations.add(downStation);
        addNextStation(stations, downStation);
        return stations;
    }

    private void addNextStation(List<Station> stations, Station downStation) {
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findNextStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }
    }

    private Optional<Section> findNextStation(Station finalDownStation) {
        return sections.stream()
                .filter(it -> it.isUpStationEqualsToStation(finalDownStation))
                .findFirst();
    }

    private Station findFirstUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> previousLineStation = findPreviousStation(finalDownStation);
            if (!previousLineStation.isPresent()) {
                break;
            }
            downStation = previousLineStation.get().getUpStation();
        }

        return downStation;
    }

    private Optional<Section> findPreviousStation(Station finalDownStation) {
        return sections.stream()
                .filter(it -> it.isDownStationEqualsToStation(finalDownStation))
                .findFirst();
    }

    public void add(Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(section::isUpStationEqualsToStation);
        boolean isDownStationExisted = stations.stream().anyMatch(section::isDownStationEqualsToStation);

        if (stations.isEmpty()) {
            sections.add(section);
            return;
        }

        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException(EXISTS_SECTION_EXCEPTION_MESSAGE);
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new IllegalArgumentException(NOT_EXISTS_ALL_STATIONS_EXCEPTION_MESSAGE);
        }

        if (isUpStationExisted) {
            sections.stream()
                    .filter(section::isUpStationEqualsToUpStationInSection)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section));
        }

        if (isDownStationExisted) {
            sections.stream()
                    .filter(section::isDownStationEqualsToDownStationInSection)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section));
        }

        sections.add(section);
    }

    public void removeSection(Line line, Station station) {
        if (sections.size() <= 1) {
            throw new IllegalArgumentException(AT_LEAST_ONE_SECTION_EXCEPTION_MESSAGE);
        }

        Optional<Section> upLineStation = findNextStation(station);
        Optional<Section> downLineStation = findPreviousStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            Distance newDistance = Distance.merge(upLineStation.get().getDistance(), downLineStation.get().getDistance());
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);
    }
}
