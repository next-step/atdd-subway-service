package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections;

    protected Sections() {
    }

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections createEmpty() {
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

        if (!stations.isEmpty() && stations.stream().noneMatch(it1 -> it1 == section.getUpStation()) &&
                stations.stream().noneMatch(it1 -> it1 == section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void updateUpStation(Section section) {
        List<Station> stations = this.getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == section.getUpStation());

        if (isUpStationExisted) {
            this.sections.stream()
                    .filter(it -> it.getUpStation() == section.getUpStation())
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

        }
    }

    private void updateDownStation(Section section) {
        List<Station> stations = this.getStations();
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == section.getDownStation());

        if (isDownStationExisted) {
            this.sections.stream()
                    .filter(it -> it.getDownStation() == section.getDownStation())
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

        }
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(this.sections);
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

    public void remove(Station station) {
        checkValidRemovableStatus();

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
            this.add(new Section(downLineStation.get().getLine(), newUpStation, newDownStation, newDistance));
        }

    }

    private void checkValidRemovableStatus() {
        if (this.sections.size() <= 1) {
            throw new RuntimeException();
        }
    }
}
