package nextstep.subway.line.domain;

import nextstep.subway.common.exception.StationNotFoundException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    public static final int SECTION_SIZE = 1;
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {}

    public Sections(Section section) {
        sections.add(section);
    }

    public static Sections of(Line line, Station upStation, Station downStation, int distance) {
        return new Sections(new Section(line,upStation, downStation, distance));
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
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == section.getUpStation());
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == section.getDownStation());

        if (isNotAddPossibleStation(section, stations, isUpStationExisted, isDownStationExisted)) {
            return;
        }

        addPossibleStation(section, isUpStationExisted, isDownStationExisted);
    }

    public void removeStation(Line line, Station station) {
        validateExistStation(station);
        removeStationExecution(line, station);
    }

    private Station findStartStation() {
        Section upStation = getSections().stream().findFirst().orElseThrow(() -> new StationNotFoundException("존재하지 않는 역입니다."));
        return findDownStation(upStation.getUpStation());
    }

    private Station findDownStation(Station station) {
        Optional<Section> nextLineStation = findDownLineStation(station);
        while (nextLineStation.isPresent()) {
            station = nextLineStation.get().getUpStation();
            nextLineStation = findDownLineStation(nextLineStation.get().getUpStation());
        }

        return station;
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

    private Optional<Section> findDownLineStation(Station upStation) {
        return getSections().stream()
                .filter(it -> it.getDownStation() == upStation)
                .findFirst();
    }

    private Optional<Section> findUpLineStation(Station downStation) {
        return getSections().stream()
                .filter(it -> it.getUpStation() == downStation)
                .findFirst();
    }

    private boolean isNotAddPossibleStation(Section section, List<Station> stations, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
           throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && isNoneMatchUpStation(section, stations) && isNoneMatchDownStation(section, stations)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            getSections().add(section);
            return true;
        }

        return false;
    }

    private boolean isNoneMatchUpStation(Section section, List<Station> stations) {
        return stations.stream().noneMatch(it -> it.equals(section.getUpStation()));
    }

    private boolean isNoneMatchDownStation(Section section, List<Station> stations) {
        return stations.stream().noneMatch(it -> it.equals(section.getDownStation()));
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
                .filter(it -> it.getDownStation() == section.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

        getSections().add(section);
    }

    private void addUpStationExisted(Section section) {
        getSections().stream()
                .filter(it -> it.getUpStation() == section.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

        getSections().add(section);
    }

    private void validateExistStation(Station station) {
        if (getSections().size() <= SECTION_SIZE) {
            throw new IllegalArgumentException("구간에서 역을 제거할 수 없습니다.");
        }
        getStations().stream()
                .filter(it -> it == station)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("제거 할 역이 노선에 없습니다."));
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

    private void addNewSection(Line line, Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        add(new Section(line, newUpStation, newDownStation, newDistance));
    }

}