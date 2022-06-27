package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> values;

    protected Sections() {
        this(new ArrayList<>());
    }

    protected Sections(List<Section> values) {
        this.values = copy(values);
    }

    private static List<Section> copy(List<Section> sections) {
        return sections.stream().map(Section::from).collect(Collectors.toList());
    }

    public List<Section> get() {
        return Collections.unmodifiableList(values);
    }

    public void add(Section section) {
        validateSectionToAdd(section);

        if (values.isEmpty()) {
            values.add(section);
            return;
        }

        updateExistingSection(section);

        values.add(section);
    }

    private void validateSectionToAdd(Section section) {
        List<Station> allStations = getStations();

        boolean isUpStationExisted = allStations.contains(section.getUpStation());
        boolean isDownStationExisted = allStations.contains(section.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }

        if (!allStations.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    private void updateExistingSection(Section newSection) {
        values.stream()
                .filter(section -> section.hasSameUpOrDownStation(newSection))
                .findFirst()
                .ifPresent(section -> section.update(newSection));
    }

    public void remove(Station station) {
        validateMinimumNumberOfSections();

        Optional<Section> upLineStation = findSectionByUpStation(station);
        Optional<Section> downLineStation = findSectionByDownStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section sectionWithUpStation = upLineStation.get();
            Section sectionWithDownStation = downLineStation.get();
            values.add(sectionWithUpStation.merge(sectionWithDownStation));
        }

        upLineStation.ifPresent(it -> values.remove(it));
        downLineStation.ifPresent(it -> values.remove(it));
    }

    private void validateMinimumNumberOfSections() {
        if (values.size() <= 1) {
            throw new IllegalStateException("구간이 하나인 노선에서 역을 삭제할 수 없습니다.");
        }
    }

    private Optional<Section> findSectionByUpStation(Station upStation) {
        return values.stream().filter(it -> it.hasUpStation(upStation)).findFirst();
    }

    private Optional<Section> findSectionByDownStation(Station downStation) {
        return values.stream().filter(it -> it.hasDownStation(downStation)).findFirst();
    }

    public List<Station> getStations() {
        if (values.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findFinalUpStation();
        stations.add(downStation);

        return findAllDownStation(stations, downStation);
    }

    public Station findFinalUpStation() {
        if (values.isEmpty()) {
            return null;
        }
        return findUpStation(values.get(0).getUpStation());
    }

    private List<Station> findAllDownStation(List<Station> stations, Station finalUpStation) {
        Optional<Section> nextLineStation = findSectionByUpStation(finalUpStation);
        if (!nextLineStation.isPresent()) {
            return stations;
        }
        Station newStation = nextLineStation.get().getDownStation();
        List<Station> newStations = Stream.concat(stations.stream(), Stream.of(newStation))
                .collect(Collectors.toList());
        return findAllDownStation(newStations, newStation);
    }

    private Station findUpStation(Station downStation) {
        Optional<Section> nextLineStation = findSectionByDownStation(downStation);
        if (!nextLineStation.isPresent()) {
            return downStation;
        }
        return findUpStation(nextLineStation.get().getUpStation());
    }
}
