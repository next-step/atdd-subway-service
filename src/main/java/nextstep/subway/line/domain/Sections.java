package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    private static final int ESSENTIAL_SECTION_QTY = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sectionElements = new ArrayList<>();

    protected Sections() {

    }

    public Sections(Section section) {
        valid(section);
        sectionElements.add(section);
    }

    private void valid(Section section) {
        if (section == null) {
            throw new IllegalArgumentException("하나의 구간은 꼭 필요합니다.");
        }
    }


    public List<Station> getOrderStations() {
        List<Station> stations = new ArrayList<>();
        Station station = findFinalUpStation();

        while (existNextStation(station)) {
            stations.add(station);
            station = nextStation(station);
        }

        return Collections.unmodifiableList(stations);
    }

    private Station nextStation(Station station) {
        return this.sectionElements.stream()
                .filter((section -> section.isUpStation(station)))
                .findFirst()
                .map(Section::getDownStation)
                .orElse(null);
    }

    private Station findFinalUpStation() {
        Stations downStations = Stations.createDownStations(this);
        Stations upStations = Stations.createUpStations(this);

        return upStations.isNotContainsFirstStation(downStations)
                .orElseThrow(() -> new NoSuchElementException("하행 종점을 찾을수 없습니다."));
    }

    private boolean existNextStation(Station station) {
        return !Station.isEmpty(station) ;
    }


    public void addSection(Section section) {
        validAddSection(section);
        if (this.sectionElements.isEmpty()) {
            sectionElements.add(section);
            return;
        }

        if (isUpStationExisted(section)) {
            upStationExistedAddSection(section);
            return;
        }
        downStationExistedAddSection(section);
    }

    private void validAddSection(Section section) {
        duplicateAddSectionValid(section);
        existUpAndDownStationAddSectionValid(section);
    }


    private void upStationExistedAddSection(Section section) {
        sectionElements.stream()
                .filter(it -> it.isUpStation(section.getUpStation()))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

        sectionElements.add(section);
    }

    private void downStationExistedAddSection(Section section) {
        sectionElements.stream()
                .filter(it -> it.isDownStation(section.getDownStation()))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

        sectionElements.add(section);
    }


    private void existUpAndDownStationAddSectionValid(Section section) {
        final boolean isValid = getOrderStations()
                .stream()
                .anyMatch(section::isContainStation);

        if (!isValid) {
            throw new IllegalArgumentException("등록 할수 없는 구간입니다.");
        }
    }

    private void duplicateAddSectionValid(Section section) {
        if (sectionElements.contains(section)) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
    }

    private boolean isUpStationExisted(Section section) {
        return sectionElements.stream().anyMatch(it -> it.isUpStation(section.getUpStation()));
    }

    public void removeStation(Station station) {
        validRemoveStation();
        Optional<Section> upLineStation = findSectionByDownStation(station);
        Optional<Section> downLineStation = findSectionByUpStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            mergeSection(upLineStation.get(), downLineStation.get());
        }

        upLineStation.ifPresent(it -> sectionElements.remove(it));
        downLineStation.ifPresent(it -> sectionElements.remove(it));
    }

    private void validRemoveStation() {
        if (this.sectionElements.size() <= ESSENTIAL_SECTION_QTY) {
            throw new IllegalArgumentException("구간은 꼭 하나만 있어야 합니다.");
        }
    }

    private Optional<Section> findSectionByDownStation(Station station) {
        return sectionElements.stream()
                .filter(it -> it.isUpStation(station))
                .findFirst();
    }

    private Optional<Section> findSectionByUpStation(Station station) {
        return sectionElements.stream()
                .filter(it -> it.isDownStation(station))
                .findFirst();
    }

    private void mergeSection(Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        Distance newDistance = Distance.sumDistance(upLineStation.getDistance(), downLineStation.getDistance());

        sectionElements.add(new Section(downLineStation.getLine(), newUpStation, newDownStation, newDistance));
    }

    public List<Section> getSectionElements() {
        return Collections.unmodifiableList(sectionElements);

    }
}
