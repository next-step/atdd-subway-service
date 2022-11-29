package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Section> getSections(){
        return this.sections;
    }

    public List<StationResponse> getStationResponse() {
        List<Station> stations = getStations();
        return stations.stream().map(StationResponse::of).collect(Collectors.toList());
    }

    public List<Station> getStations(){
        if(this.sections.isEmpty()){
            return new ArrayList<>();
        }
        List<Station> allStations = new ArrayList<>();
        allStations.add(findUpStation());
        allStations.addAll(findStations(allStations.get(0)));
        return allStations;
    }

    public void add(Section section) {
        sections.add(section);
    }

    private Station findUpStation() {
        Station downStation = this.sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.sections.stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    private List<Station> findStations(Station startStation){
        List<Station> stations = new ArrayList<>();
        Station downStation = startStation;
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.sections.stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }
        return stations;
    }
}
