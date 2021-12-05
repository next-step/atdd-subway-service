package nextstep.subway.line.domain;

import nextstep.subway.line.exception.SectionException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class Sections {
    private static final String STATION_NOT_EXIST_MESSAGE = "등록할 수 없는 구간 입니다.";
    private static final String SECTION_DUPLICATE_MESSAGE = "이미 등록된 구간 입니다.";
    private static final String SECTION_NOT_DELETE_MESSAGE = "노선에 마지막 구간은 제거 할 수 없습니다.";
    private static final String SECTION_NOT_EXIST_MESSAGE = "제거할 구간이 없습니다.";
    private static final int SECTIONS_MIN_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        if (!sections.isEmpty()) {
            validate(section);

            sections.stream()
                    .filter(section1 -> section1.getUpStation() == section.getUpStation())
                    .findFirst()
                    .ifPresent(section1 -> section1.updateUpStation(section.getDownStation(), section.getDistance()));

            sections.stream()
                    .filter(section1 -> section1.getDownStation() == section.getDownStation())
                    .findFirst()
                    .ifPresent(section1 -> section1.updateDownStation(section.getUpStation(), section.getDistance()));
        }

        sections.add(section);
    }

    private void validate(Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(section1 -> section1 == section.getUpStation());
        boolean isDownStationExisted = stations.stream().anyMatch(section1 -> section1 == section.getDownStation());
        if (isUpStationExisted && isDownStationExisted) {
            throw new SectionException(SECTION_DUPLICATE_MESSAGE);
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(section1 -> section1 == section.getUpStation()) &&
                stations.stream().noneMatch(section1 -> section1 == section.getDownStation())) {
            throw new SectionException(STATION_NOT_EXIST_MESSAGE);
        }
    }

    public List<Station> getStations() {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }

    public void remove(Line line, Station station) {
        validateRemove(station);

        if (isBetween(station)) {
            merge(station);
            return;
        }

        sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst()
                .ifPresent(it -> sections.remove(it));

        sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst()
                .ifPresent(it -> sections.remove(it));
    }

    private void validateRemove(Station station) {
        sections.stream()
                .filter(section -> section.getUpStation() == station || section.getDownStation() == station)
                .findFirst()
                .orElseThrow(() -> new SectionException(SECTION_NOT_EXIST_MESSAGE));

        if (sections.size() <= SECTIONS_MIN_SIZE) {
            throw new SectionException(SECTION_NOT_DELETE_MESSAGE);
        }
    }

    public List<Station> getOrderedStations(Line line) {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation(line);
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(section -> section.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation(Line line) {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(section -> section.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    private void merge(Station station) {
        Section upSection = sections.stream()
                .filter(section -> section.getUpStation() == station)
                .findFirst()
                .orElseThrow(() -> new SectionException(SECTION_NOT_EXIST_MESSAGE));

        sections.stream()
                .filter(section -> section.getDownStation() == station)
                .findFirst()
                .ifPresent(section -> section.mergeStation(upSection));

        sections.remove(upSection);
    }

    private boolean isBetween(Station station) {
        return isMatchDownStation(station) && isMatchUpStation(station);
    }

    private boolean isMatchUpStation(Station station) {
        return sections.stream()
                .filter(section -> section.getUpStation() == station)
                .findFirst()
                .isPresent();
    }

    private boolean isMatchDownStation(Station station) {
        return sections.stream()
                .filter(section -> section.getDownStation() == station)
                .findFirst()
                .isPresent();
    }
}
