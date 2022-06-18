package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sectionElements = new ArrayList<>();

    protected Sections() {

    }

    public Sections(Section section) {
        sectionElements.add(section);
    }

    public int size() {
        return sectionElements.size();
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
                .filter(it -> it.getUpStation().equals(section.getUpStation()))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
        sectionElements.add(section);
    }

    private void downStationExistedAddSection(Section section) {
        sectionElements.stream()
                .filter(it -> it.getDownStation().equals(section.getDownStation()) )
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

        sectionElements.add(section);
    }


    private void existUpAndDownStationAddSectionValid(Section section) {
        final boolean isValid = getStations().stream()
                .anyMatch((station ->
                        station.equals(section.getUpStation()) || station.equals(section.getDownStation()))
                );

        if (!isValid) {
            throw new IllegalArgumentException("등록 할수 없는 구간입니다.");
        }
    }

    private void duplicateAddSectionValid(Section section) {
        if (isUpStationExisted(section) && isDownStationExisted(section)) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
    }

    private boolean isDownStationExisted(Section section) {
        return this.getSectionElements().stream().anyMatch(it -> it.getDownStation().equals(section.getDownStation()));
    }

    private boolean isUpStationExisted(Section section) {
        return this.getSectionElements().stream().anyMatch(it -> it.getUpStation().equals(section.getUpStation()));
    }


    public List<Section> getSectionElements() {
        return sectionElements;
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        Station station = findFinalUpStation();
        while (station != null) {
            stations.add(station);
            station = nextStation(station);
        }
        return stations;
    }

    private Station nextStation(Station station) {
        return this.sectionElements.stream()
                .filter((section -> section.getUpStation().equals(station)))
                .findFirst()
                .map(Section::getDownStation)
                .orElse(null);
    }

    private Station findFinalUpStation() {
        List<Station> downStations = downStations();
        List<Station> upStations = upStations();

        return upStations.stream()
                .filter((upStation) -> !downStations.contains(upStation))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("하행 종점을 찾을수 없습니다."));
    }

    private List<Station> downStations() {
        return sectionElements.stream().map(Section::getDownStation).collect(Collectors.toList());
    }

    private List<Station> upStations() {
        return sectionElements.stream().map(Section::getUpStation).collect(Collectors.toList());
    }



//    public List<Station> getStations() {
//
//    }


}
