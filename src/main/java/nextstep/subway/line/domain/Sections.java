package nextstep.subway.line.domain;

import nextstep.subway.line.exception.SectionException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
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

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "line_id")
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        if (!sections.isEmpty()) {
            validate(section);

            sections.stream()
                    .filter(it -> it.getUpStation() == section.getUpStation())
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

            sections.stream()
                    .filter(it -> it.getDownStation() == section.getDownStation())
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
        }

        sections.add(section);
    }

    private void validate(Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == section.getUpStation());
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == section.getDownStation());
        if (isUpStationExisted && isDownStationExisted) {
            throw new SectionException(SECTION_DUPLICATE_MESSAGE);
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == section.getUpStation()) &&
                stations.stream().noneMatch(it -> it == section.getDownStation())) {
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

    private void validateRemove(Station station) {
        sections.stream()
                .filter(it -> it.getUpStation() == station || it.getDownStation() == station)
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

    private Station findUpStation(Line line) {
        Station downStation = sections.get(0).getUpStation();
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
