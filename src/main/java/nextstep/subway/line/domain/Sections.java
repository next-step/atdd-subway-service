package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sectionList = new ArrayList<>();

    protected Sections() {

    }

    public List<Section> getSections() {
        return new ArrayList<>(sectionList);
    }

    public void add(Section section) {
        validateNewSection(section);

        List<Station> stations = getStations();
        if (stations.isEmpty()) {
            sectionList.add(section);
            return;
        }

        boolean isUpStationExisted = hasStation(section.getUpStation());
        boolean isDownStationExisted = hasStation(section.getDownStation());

        if (isUpStationExisted) {
            sectionList.stream()
                    .filter(it -> it.getUpStation() == section.getUpStation())
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

            sectionList.add(section);
        } else if (isDownStationExisted) {
            sectionList.stream()
                    .filter(it -> it.getDownStation() == section.getDownStation())
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

            sectionList.add(section);
        } else {
            throw new RuntimeException();
        }
    }

    private void validateNewSection(Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == section.getUpStation());
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == section.getDownStation());
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == section.getUpStation()) &&
                stations.stream().noneMatch(it -> it == section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public void remove(Section section) {
        sectionList.remove(section);
    }

    public List<Station> getStations() {
        if (sectionList.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sectionList.stream()
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

    private boolean hasStation(Station station) {
        return getStations().stream().anyMatch(it -> it == station);
    }

    private Station findUpStation() {
        Station downStation = sectionList.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sectionList.stream()
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
