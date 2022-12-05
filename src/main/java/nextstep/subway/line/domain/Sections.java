package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void add(Section section) {
        sections.add(section);
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        if(findFinalUpStation().isPresent()){
            addStations(stations);
        }

        return stations;
    }

    private void addStations(List<Station> stations){
        Station downStation = findFinalUpStation().get().getUpStation();
        stations.add(downStation);

        while (downStation != null) {
            downStation = ifPresentAddStation(downStation, stations);
        }
    }

    private Optional<Section> findFinalUpStation() {
        return sections.stream()
                .filter(section -> !section.anyDownStation(sections))
                .findFirst();
    }

    private Station ifPresentAddStation(Station upStation, List<Station> stations) {
        Optional<Section> nextLineStation = findSectionByUpStation(upStation);
        if(!nextLineStation.isPresent()){
            return null;
        }
        nextLineStation.ifPresent(section -> stations.add(section.getDownStation()));
        return nextLineStation.get().getDownStation();
    }


    private Optional<Section> findSectionByUpStation(Station upStation) {
        return sections.stream()
                .filter(it -> it.isUpStation(upStation))
                .findFirst();
    }

    public List<Section> getSections() {
        return sections;
    }
}
