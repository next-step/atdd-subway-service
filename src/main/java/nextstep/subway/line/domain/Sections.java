package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections(){}

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public boolean add(Section section) {
        Stations stations = new Stations(getStations());
        if (stations.isEmpty()) {
            return sections.add(section);
        }

        boolean isUpStationExisted = stations.contains(section.getUpStation());
        boolean isDownStationExisted = stations.contains(section.getDownStation());

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (isUpStationExisted) {
            sections.stream()
                    .filter(it -> it.getUpStation() == section.getUpStation())
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
            return sections.add(section);
        }

        sections.stream()
                .filter(it -> it.getDownStation() == section.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
        return sections.add(section);
    }

    public boolean remove(Section section) {
        return sections.remove(section);
    }


    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
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

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void removeStation(Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }
        Line line = sections.get(0).getLine();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.equalsUpstation(station))
                .findFirst();
        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.equalsDownStation(station))
                .findFirst();

        upLineStation.ifPresent(this::remove);
        downLineStation.ifPresent(this::remove);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section upSection = upLineStation.get();
            Section downSection = downLineStation.get();

            int newDistance = upSection.getDistance() + downSection.getDistance();
            add(new Section(line, upSection.getUpStation(), downSection.getDownStation(), newDistance));
        }
    }
}
