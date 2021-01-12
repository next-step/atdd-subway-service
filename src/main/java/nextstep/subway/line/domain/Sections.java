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

    protected Sections() {}

    public Sections(Section section) {
        sections.add(section);
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections of(Line line, Station upStation, Station downStation, int distance) {
        return new Sections(new Section(line,upStation, downStation, distance));
    }

    public static Sections of() {
        return new Sections();
    }

    public List<Section> getSections() {
        return sections;
    }

    public void add(Section section) {
        sections.add(section);
    }

    public void remove(Section section) {
        sections.remove(section);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station startStation = findStartStation();
        addAllLineStation(stations, startStation);

        return stations;
    }

    public void addStation(Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = isExistedStation(stations, section.getUpStation());
        boolean isDownStationExisted = isExistedStation(stations, section.getDownStation());

        if (isNotAddPossibleStation(section, stations, isUpStationExisted, isDownStationExisted)) return;

        addPossibleStation(section, isUpStationExisted, isDownStationExisted);
    }

    public void removeStation(Line line, Station station) {
        validateExistStation(station);
        removeStationExecution(line, station);
    }

    private Station findStartStation() {
        Station upStation = getSections().get(0).getUpStation();
        return findDownStation(upStation);
    }

    private Station findDownStation(Station station) {
        Optional<Section> nextLineStation = findDownLineStation(station);
        while (nextLineStation.isPresent()) {
            station = nextLineStation.get().getUpStation();
            nextLineStation = findDownLineStation(nextLineStation.get().getUpStation());
        }
        return station;
    }

    private Optional<Section> findDownLineStation(Station upStation) {
        return getSections().stream()
                .filter(it -> isEqualStation(it.getDownStation(), upStation))
                .findFirst();
    }

    private Optional<Section> findUpLineStation(Station downStation) {
        return getSections().stream()
                .filter(it -> isEqualStation(it.getUpStation(), downStation))
                .findFirst();
    }

    private void removeStationExecution(Line line, Station station) {
        Optional<Section> upLineStation = findUpLineStation(station);
        Optional<Section> downLineStation = findDownLineStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            addNewSection(line, upLineStation.get(), downLineStation.get());
        }

        upLineStation.ifPresent(this::remove);
        downLineStation.ifPresent(this::remove);
    }

    private void addAllLineStation(List<Station> stations, Station station) {
        stations.add(station);
        Optional<Section> nextLineStation = findUpLineStation(station);
        while (nextLineStation.isPresent()) {
            station = nextLineStation.get().getDownStation();
            stations.add(station);
            nextLineStation = findUpLineStation(nextLineStation.get().getDownStation());
        }
    }

    private void addPossibleStation(Section section, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted) {
            addUpStationExisted(section);
            return;
        }
        if (isDownStationExisted) {
            addDownStationExisted(section);
        }
    }

    private void addDownStationExisted(Section section) {
        getSections().stream()
                .filter(it -> isEqualStation(it.getDownStation(), section.getDownStation()))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

        getSections().add(section);
    }

    private void addUpStationExisted(Section section) {
        getSections().stream()
                .filter(it -> isEqualStation(it.getUpStation(), section.getUpStation()))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

        getSections().add(section);
    }

    private void addNewSection(Line line, Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        add(new Section(line, newUpStation, newDownStation, newDistance));
    }

    private void validateExistStation(Station station) {
        if (getSections().size() <= 1) {
            throw new IllegalArgumentException("구간에서 역을 제거할 수 없습니다.");
        }
        if (getStations().stream().noneMatch(it -> isEqualStation(it, station))) {
            throw new IllegalArgumentException("제거 할 역이 노선에 없습니다.");
        }
    }

    private boolean isNotAddPossibleStation(Section section, List<Station> stations, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
        if (!stations.isEmpty() && isNoneMatchStation(section.getUpStation(), stations) && isNoneMatchStation(section.getDownStation(), stations)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
        if (stations.isEmpty()) {
            getSections().add(section);
            return true;
        }
        return false;
    }

    private boolean isNoneMatchStation(Station station, List<Station> stations) {
        return stations.stream().noneMatch(it -> isEqualStation(it, station));
    }

    private boolean isExistedStation(List<Station> stations, Station station) {
        return stations.stream().anyMatch(it -> isEqualStation(it, station));
    }

    private boolean isEqualStation(Station firstStation, Station secondStation) {
        return firstStation.isEqualTo(secondStation);
    }
}
