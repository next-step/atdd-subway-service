package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> elements = new ArrayList<>();

    protected Sections() {
    }

    public void add(Section newSection) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = containsStation(stations, newSection.getUpStation());
        boolean isDownStationExisted = containsStation(stations, newSection.getDownStation());

        validateAdd(stations, isUpStationExisted, isDownStationExisted);

        if (stations.isEmpty()) {
            elements.add(newSection);
            return;
        }

        if (isUpStationExisted) {
            sectionMatchedUpStation(newSection.getUpStation())
                    .ifPresent(section -> section.updateUpStation(newSection.getDownStation(), newSection.getDistance()));
            elements.add(newSection);
            return;
        }

        if (isDownStationExisted) {
            sectionMatchedDownStation(newSection.getDownStation())
                    .ifPresent(section -> section.updateDownStation(newSection.getUpStation(), newSection.getDistance()));
            elements.add(newSection);
            return;
        }

        throw new RuntimeException();
    }

    private static boolean containsStation(List<Station> stations, Station newStation) {
        return stations.stream()
                .anyMatch(station -> Objects.equals(station, newStation));
    }

    private void validateAdd(List<Station> stations, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
        if (!stations.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public List<Station> getStations() {
        if (elements.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextSection = sectionMatchedUpStation(finalDownStation);
            if (!nextSection.isPresent()) {
                break;
            }
            downStation = nextSection.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = elements.get(0).getUpStation();

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextSection = sectionMatchedDownStation(finalDownStation);
            if (!nextSection.isPresent()) {
                break;
            }
            downStation = nextSection.get().getUpStation();
        }

        return downStation;
    }

    public void removeStation(Line line, Station station) {
        validateRemove();

        Optional<Section> upSection = sectionMatchedUpStation(station);
        Optional<Section> downSection = sectionMatchedDownStation(station);

        if (upSection.isPresent() && downSection.isPresent()) {
            Station newUpStation = downSection.get().getUpStation();
            Station newDownStation = upSection.get().getDownStation();
            int newDistance = upSection.get().getDistance() + downSection.get().getDistance();
            elements.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upSection.ifPresent(section -> elements.remove(section));
        downSection.ifPresent(section -> elements.remove(section));
    }

    private void validateRemove() {
        if (elements.size() < Section.MIN_STATION_SIZE) {
            throw new RuntimeException("구간이 없는 노선입니다.");
        }
    }

    private Optional<Section> sectionMatchedUpStation(Station station) {
        return elements.stream()
                .filter(section -> section.getUpStation() == station)
                .findFirst();
    }

    private Optional<Section> sectionMatchedDownStation(Station station) {
        return elements.stream()
                .filter(section -> section.getDownStation() == station)
                .findFirst();
    }

    public List<Section> get() {
        return elements;
    }
}
