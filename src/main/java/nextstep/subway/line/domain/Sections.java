package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

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
        this.sections.add(section);
    }

    private void checkUniqueSection(Section section) {
        if (this.sections.contains(section)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void checkValidSection(Section section) {
        List<Station> stations = this.getStations();

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> section.isUpStation(it)) &&
                stations.stream().noneMatch(it -> section.isDownStation(it))) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void updateUpStation(Section newSection) {
        List<Station> stations = this.getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(newSection::isUpStation);

        if (isUpStationExisted) {
            this.sections.stream()
                    .filter(se -> newSection.isUpStation(se.getUpStation()))
                    .findFirst()
                    .ifPresent(se -> se.updateUpStation(newSection.getDownStation(), newSection.getDistance()));

        }
    }

    private void updateDownStation(Section newSection) {
        List<Station> stations = this.getStations();
        boolean isDownStationExisted = stations.stream().anyMatch(newSection::isDownStation);

        if (isDownStationExisted) {
            this.sections.stream()
                    .filter(se -> newSection.isDownStation(se.getDownStation()))
                    .findFirst()
                    .ifPresent(se-> se.updateDownStation(newSection.getUpStation(), newSection.getDistance()));

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
        Station downStation = sections.get(0).getUpStation();
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

    public void remove(Station station) {
        validSectionSize();

        Optional<Section> upLineStation = this.sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = this.sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        upLineStation.ifPresent(it -> this.sections.remove(it));
        downLineStation.ifPresent(it -> this.sections.remove(it));

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            this.add(new Section(upLineStation.get().getLine(), newUpStation, newDownStation, newDistance));
        }
    }

    private void validSectionSize() {
        if (this.sections.size() <= 1) {
            throw new RuntimeException();
        }
    }
}
