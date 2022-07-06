package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;

public class Stations {
    List<Station> stations = new ArrayList<>();

    public Stations() {
    }

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void add(Station station) {
        stations.add(station);
    }

    public static Stations from(Sections sections) {
        Stations stations = new Stations();
        Station downStation = findUpStation(sections);
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.getSections().stream()
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

    private static Station findUpStation(Sections sections) {
        Station downStation = sections.getSections().get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.getSections().stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }
}