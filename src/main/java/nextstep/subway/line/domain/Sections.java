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
        sectionElements.add(section);
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
