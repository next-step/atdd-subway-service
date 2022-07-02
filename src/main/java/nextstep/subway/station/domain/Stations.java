package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nextstep.subway.BaseEntity;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;

public class Stations extends BaseEntity {

    List<Station> stations = new ArrayList<>();

    public Stations() {
    }

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }

    public boolean isUpStationExisted(Station upStation) {
        return stations.stream().anyMatch(it -> it == upStation);
    }

    public boolean isDownStationExisted(Station downStation) {
        return stations.stream().anyMatch(it -> it == downStation);
    }

    public void checkAddLineStation(Station upStation, Station downStation, boolean isUpStationExisted,
                                    boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
                stations.stream().noneMatch(it -> it == downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public boolean isEmpty() {
        return stations.isEmpty();
    }

    public void add(Station station) {
        stations.add(station);
    }

    public static Stations of(Sections sections) {
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