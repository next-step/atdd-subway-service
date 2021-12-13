package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    private final static int COUNT_LIMIT_FOR_REMOVE = 2;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public static Section mergedSection(Section downLineSection, Section upLineSection) {
        Station newUpStation = downLineSection.getUpStation();
        Station newDownStation = upLineSection.getDownStation();
        int newDistance = upLineSection.getDistance() + downLineSection.getDistance();
        return new Section(downLineSection.getLine(), newUpStation, newDownStation, newDistance);
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public boolean lessLimitCount() {
        return sections.size() < COUNT_LIMIT_FOR_REMOVE;
    }

    public boolean containsStation(Station station) {
        return getStations().stream()
            .anyMatch(station::equals);
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
            Optional<Section> nextLineStation = findUpLineStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }
        return stations;
    }

    public Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findDownLineStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }
        return downStation;
    }

    public Optional<Section> findUpLineStation(Station station) {
        return sections.stream()
            .filter(it -> it.isUpStation(station))
            .findFirst();
    }

    public Optional<Section> findDownLineStation(Station station) {
        return sections.stream()
            .filter(it -> it.isDownStation(station))
            .findFirst();
    }

    private void validateAddable(Section section) {
        if (alreadyAdded(section)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
        if (stationsNotAdded(section)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public void addLineStation(Section section) {
        validateAddable(section);
        boolean isUpStationExisted = containsStation(section.getUpStation());

        if (getStations().isEmpty()) {
            sections.add(section);
            return;
        }

        if (isUpStationExisted) {
            findUpLineStation(section.getUpStation())
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
            sections.add(section);
            return;
        }

        findDownLineStation(section.getDownStation())
            .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
        sections.add(section);
    }

    public void removeLineStation(Station station) {
        if (lessLimitCount()) {
            throw new RuntimeException();
        }
        Optional<Section> upLineStation = findUpLineStation(station);
        Optional<Section> downLineStation = findDownLineStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            sections.add(Sections.mergedSection(downLineStation.get(), upLineStation.get()));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    /* section 의 up, down station 이 이미 모두 등록됨 */
    public boolean alreadyAdded(Section section) {
        return containsStation(section.getDownStation()) && containsStation(section.getUpStation());
    }

    /* section 의 up, down station 이 등록되지 않음 */
    public boolean stationsNotAdded(Section section) {
        return !getSections().isEmpty() && !containsStation(section.getDownStation())
            && !containsStation(section.getUpStation());
    }
}
