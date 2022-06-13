package nextstep.subway.line.domain;

import nextstep.subway.exception.BothUpDownDoNotExistException;
import nextstep.subway.exception.MinimumSectionRemoveException;
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
    private static final String UNREGISTERABLE_SECTION_MESSAGE = "등록할 수 없는 구간 입니다.";
    private static final int MINIMUM_SIZE = 1;
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        validateAddSection(section);

        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        if (isAdded(section)) {
            return;
        }

        throw new BothUpDownDoNotExistException(UNREGISTERABLE_SECTION_MESSAGE);
    }

    private boolean isAdded(Section section) {
        if (hasStation(section.getUpStation())) {
            getUpLineStation(section.getUpStation())
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

            sections.add(section);
            return true;
        } else if (hasStation(section.getDownStation())) {
            getDownLineStation(section.getDownStation())
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

            sections.add(section);
            return true;
        }
        return false;
    }

    public List<Section> getSections() {
        return sections;
    }

    public Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getDownLineStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getUpLineStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    public boolean hasStation(Station station) {
        return getStations().stream()
                .anyMatch(item -> item == station);
    }

    public void remove(Line line, Station station) {
        if (sections.size() <= MINIMUM_SIZE) {
            throw new MinimumSectionRemoveException("최소 구간은 한개이상 존재해야합니다.");
        }

        Optional<Section> upLineStation = getUpLineStation(station);
        Optional<Section> downLineStation = getDownLineStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            Distance upDistance = upLineStation.get().getDistance();
            Distance downDistance = downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, upDistance.plus(downDistance)));
        }

        upLineStation.ifPresent(it -> line.getSections().remove(it));
        downLineStation.ifPresent(it -> line.getSections().remove(it));
    }

    private Optional<Section> getDownLineStation(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    private Optional<Section> getUpLineStation(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    private void validateAddSection(Section section) {
        if (hasStation(section.getUpStation()) && hasStation(section.getDownStation())) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
        List<Station> stations = getStations();
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == section.getUpStation()) &&
                stations.stream().noneMatch(it -> it == section.getDownStation())) {
            throw new BothUpDownDoNotExistException(UNREGISTERABLE_SECTION_MESSAGE);
        }
    }
}
