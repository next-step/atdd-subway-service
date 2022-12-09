package nextstep.subway.line.domain;

import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    private static final String ALREADY_EXITS_SECTION = "이미 등록된 구간 입니다.";
    private static final String CAN_NOT_INSERT_INVALID_SECTION = "등록할 수 없는 구간 입니다.";
    private static final String CAN_NOT_DELETE_LAST_SECTION_IN_LINE = "노선에 구간이 하나 뿐인 경우에는 삭제할 수 없습니다.";

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        List<Station> stations = getStations();
        validateSection(stations, section);

        if (stations.isEmpty()) {
            sections.add(section);
            return;
        }

        if (isUpStationExisted(stations, section)) {
            updateUpStation(section);
            sections.add(section);
            return;
        }

        if (isDownStationExisted(stations, section)) {
            updateDownStation(section);
            sections.add(section);
        }
    }

    private void updateUpStation(Section section) {
        sections.stream()
                .filter(it -> it.hasEqualUpStation(section.getUpStation()))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void updateDownStation(Section section) {
        sections.stream()
                .filter(it -> it.hasEqualDownStation(section.getDownStation()))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    private void validateSection(List<Station> stations, Section section) {
        if (isUpStationExisted(stations, section) && isDownStationExisted(stations, section)) {
            throw new InvalidDataException(ALREADY_EXITS_SECTION);
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == section.getUpStation()) &&
                stations.stream().noneMatch(it -> it == section.getDownStation())) {
            throw new InvalidDataException(CAN_NOT_INSERT_INVALID_SECTION);
        }
    }

    private boolean isUpStationExisted(List<Station> stations, Section section) {
        return stations.stream().anyMatch(it -> it == section.getUpStation());
    }

    private boolean isDownStationExisted(List<Station> stations, Section section) {
        return stations.stream().anyMatch(it -> it == section.getDownStation());
    }

    public void removeLineStation(Line line, Station station) {

        if (sections.size() <= 1) {
            throw new InvalidDataException(CAN_NOT_DELETE_LAST_SECTION_IN_LINE);
        }

        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.hasEqualUpStation(station))
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.hasEqualDownStation(station))
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section newUpSection = downLineStation.get();
            Section newDownSection = upLineStation.get();

            Station newUpStation = newUpSection.getUpStation();
            Station newDownStation = newDownSection.getDownStation();
            sections.add(new Section(line, newUpStation, newDownStation,
                    newUpSection.getDistance().add(newDownSection.getDistance())));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station station = findFirstStation();
        stations.add(station);

        while(station != null) {
            Station nextStation = null;

            if (findNextStation(station).isPresent()) {
                nextStation  = findNextStation(station).get().getDownStation();
                stations.add(nextStation);
            }

            station = nextStation;
        }

        return stations;
    }


    private Optional<Section> findNextStation(Station station) {
        return sections.stream()
                .filter(it -> it.hasEqualUpStation(station))
                .findFirst();
    }

    private Station findFirstStation() {
        return sections.stream()
                .filter(section -> isFirstStation(section.getUpStation()))
                .findFirst()
                .get()
                .getUpStation();
    }

    private boolean isFirstStation(Station candidate) {
        return !sections.stream()
                .anyMatch(it -> it.hasEqualDownStation(candidate));
    }

    public List<Section> getSections() {
        return sections;
    }
}
