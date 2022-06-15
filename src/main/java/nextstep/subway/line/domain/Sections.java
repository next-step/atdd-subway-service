package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Embeddable
public class Sections {

    private static final int MIN_REMOVE_SECTION_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public void add(Section section) {
        boolean isUpStationExisted = isStationExisted(section.getUpStation());
        boolean isDownStationExisted = isStationExisted(section.getDownStation());

        raiseIfNotValidAddSection(isUpStationExisted, isDownStationExisted);

        if (isEmpty()) {
            sections.add(section);
            return;
        }

        if (isUpStationExisted) {
            getNextSectionByEqualUpStation(section.getUpStation())
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

            sections.add(section);
            return;
        }

        if (isDownStationExisted) {
            getNextSectionByEqualDownStation(section.getDownStation())
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

            sections.add(section);
            return;
        }
        throw new IllegalArgumentException();
    }

    private boolean isStationExisted(Station station) {
        return sections.stream()
                .anyMatch(it -> Objects.equals(station, it.getUpStation()) || Objects.equals(station, it.getDownStation()));
    }

    private void raiseIfNotValidAddSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }

        if (!sections.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public Optional<Section> getNextSectionByEqualUpStation(Station station) {
        return sections.stream()
                .filter(it -> Objects.equals(station, it.getUpStation()))
                .findFirst();
    }

    public Optional<Section> getNextSectionByEqualDownStation(Station station) {
        return sections.stream()
                .filter(it -> Objects.equals(it.getDownStation(), station))
                .findFirst();
    }

    public void remove(Line line, Station station) {
        raiseIfNotValidRemoveStation();

        Optional<Section> upLineStation = getNextSectionByEqualUpStation(station);
        Optional<Section> downLineStation = getNextSectionByEqualDownStation(station);

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            Distance newDistance = upLineStation.get().getDistance().plus(downLineStation.get().getDistance());
            add(new Section(line, newUpStation, newDownStation, newDistance));
        }
    }

    private void raiseIfNotValidRemoveStation() {
        if (sections.size() <= MIN_REMOVE_SECTION_SIZE) {
            throw new IllegalArgumentException("구간이 하나 이상일때 역을 지울 수 있습니다.");
        }
    }

    public List<Station> getOrderedStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextSection = getNextSectionByEqualUpStation(finalDownStation);
            if (!nextSection.isPresent()) {
                break;
            }
            downStation = nextSection.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextSection = getNextSectionByEqualDownStation(finalDownStation);
            if (!nextSection.isPresent()) {
                break;
            }
            downStation = nextSection.get().getUpStation();
        }

        return downStation;
    }
}
