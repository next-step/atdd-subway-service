package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections;

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections initSections() {
        return new Sections(new ArrayList<>());
    }

    public void add(Section section) {
        checkUniqueSection(section);
        checkValidSection(section);
        updateUpStation(section);
        updateDownStation(section);
        sections.add(section);
    }

    private void checkUniqueSection(Section section) {
        if (this.sections.contains(section)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void checkValidSection(Section section) {
        List<Station> stations = this.getStations();

        if (!stations.isEmpty() && stations.stream().noneMatch(s1 -> s1 == section.getUpStation()) &&
                stations.stream().noneMatch(s1 -> s1 == section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void updateUpStation(Section section) {
        List<Station> stations = this.getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(s -> s == section.getUpStation());

        if (isUpStationExisted) {
            this.sections.stream()
                    .filter(s -> s.getUpStation() == section.getUpStation())
                    .findFirst()
                    .ifPresent(s -> s.updateUpStation(section.getDownStation(), section.getDistance()));

        }
    }

    private void updateDownStation(Section section) {
        List<Station> stations = this.getStations();
        boolean isDownStationExisted = stations.stream().anyMatch(s -> s == section.getDownStation());

        if (isDownStationExisted) {
            this.sections.stream()
                    .filter(s -> s.getDownStation() == section.getDownStation())
                    .findFirst()
                    .ifPresent(s -> s.updateDownStation(section.getUpStation(), section.getDistance()));

        }
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

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


    public List<Section> getSections() {
        return sections;
    }
}
