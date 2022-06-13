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
        if (stations.isEmpty()) {
            elements.add(newSection);
            return;
        }
        validateAdd(newSection, stations);
        addSectionInMiddle(newSection);
        elements.add(newSection);
    }

    private void addSectionInMiddle(Section newSection) {
        addUpSection(newSection);
        addDownSection(newSection);
    }

    private void addDownSection(Section newSection) {
        sectionMatchedDownStation(newSection.getDownStation())
                .ifPresent(section -> section.updateDownStation(newSection.getUpStation(), newSection.getDistance()));

    }

    private void addUpSection(Section newSection) {
        sectionMatchedUpStation(newSection.getUpStation())
                .ifPresent(section -> section.updateUpStation(newSection.getDownStation(), newSection.getDistance()));
    }

    private static boolean containsStation(List<Station> stations, Station newStation) {
        return stations.stream()
                .anyMatch(station -> Objects.equals(station, newStation));
    }

    private void validateAdd(Section newSection, List<Station> stations) {
        boolean isUpStationExisted = containsStation(stations, newSection.getUpStation());
        boolean isDownStationExisted = containsStation(stations, newSection.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
        if (!stations.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    public List<Station> getStations() {
        if (elements.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        Optional<Section> nextSection = sectionMatchedUpStation(downStation);
        while (nextSection.isPresent()) {
            downStation = nextSection.get().getDownStation();
            stations.add(downStation);
            nextSection = sectionMatchedUpStation(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = elements.get(0).getUpStation();
        Optional<Section> nextSection = sectionMatchedDownStation(downStation);
        while (nextSection.isPresent()) {
            downStation = nextSection.get().getUpStation();
            nextSection = sectionMatchedDownStation(downStation);
        }
        return downStation;
    }

    public void removeStation(Line line, Station station) {
        validateRemove();
        Optional<Section> upSection = sectionMatchedUpStation(station);
        Optional<Section> downSection = sectionMatchedDownStation(station);
        if (upSection.isPresent() && downSection.isPresent()) {
            mergeSection(line, upSection.get(), downSection.get());
        }
        upSection.ifPresent(section -> elements.remove(section));
        downSection.ifPresent(section -> elements.remove(section));
    }

    private void mergeSection(Line line, Section upSection, Section downSection) {
        Station newUpStation = downSection.getUpStation();
        Station newDownStation = upSection.getDownStation();
        int newDistance = upSection.getDistance() + downSection.getDistance();
        Section newSection = new Section(line, newUpStation, newDownStation, newDistance);
        elements.add(newSection);
    }

    private void validateRemove() {
        if (elements.size() < Section.MIN_STATION_SIZE) {
            throw new IllegalArgumentException("구간이 없는 노선입니다.");
        }
    }

    private Optional<Section> sectionMatchedUpStation(Station station) {
        return elements.stream()
                .filter(section -> Objects.equals(section.getUpStation(), station))
                .findFirst();
    }

    private Optional<Section> sectionMatchedDownStation(Station station) {
        return elements.stream()
                .filter(section -> Objects.equals(section.getDownStation(), station))
                .findFirst();
    }

    public List<Section> get() {
        return elements;
    }
}
