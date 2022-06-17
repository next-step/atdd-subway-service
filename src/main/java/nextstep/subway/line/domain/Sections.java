package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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


    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        Station downStation = findFinalDownStation();
        stations.add(downStation);
        while (downStation != null) {
            Optional<Section> nextLineStation = nextSection(downStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }
        return stations;
    }

    private Optional<Section> nextSection(Station finalDownStation) {
        return this.sectionElements.stream()
                .filter(it -> it.getUpStation() == finalDownStation)
                .findFirst();
    }


    private Station findFinalDownStation() {
        Station downStation = sectionElements.get(0).getUpStation();
        while (downStation != null) {
            Optional<Section> nextLineStation = getSectionByDownStation(downStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }
        return downStation;
    }

    private Optional<Section> getSectionByDownStation(Station findDownStation) {
        return sectionElements.stream()
                .filter(it -> it.getDownStation() == findDownStation)
                .findFirst();
    }

//    public List<Station> getStations() {
//
//    }


}
