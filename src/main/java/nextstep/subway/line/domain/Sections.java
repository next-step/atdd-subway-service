package nextstep.subway.line.domain;

import nextstep.exception.BusinessException;
import nextstep.exception.ErrorCode;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    private static final int DEFAULT_LINE_FARE = 0;
    private static final int MIN_REMOVABLE_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void add(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return getStations(new ArrayList());
    }

    public List<Station> getStations(List<Station> stations) {
        Map<Station, Station> upToDowns = getUpToDownStation();
        Station upStation = getUpStation();

        while (upStation != null) {
            stations.add(upStation);
            upStation = upToDowns.get(upStation);
        }

        return stations;
    }

    private Map<Station, Station> getUpToDownStation() {
        return sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
    }

    private Station getUpStation() {
        List<Station> downStations = sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        return sections.stream()
                .map(Section::getUpStation)
                .filter(upStation -> !downStations.contains(upStation))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public void updateUpStation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    public void updateDownStation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    public boolean hasStation(Station station) {
        return getStations().stream()
                .anyMatch(it -> it == station);
    }

    public void updateStation(Station upStation, Station downStation, int distance) {
        if (hasStation(upStation)) {
            updateUpStation(upStation, downStation, distance);
        }
        if (hasStation(downStation)) {
            updateDownStation(upStation, downStation, distance);
        }
    }

    public void checkUpdatable(Station upStation, Station downStation) {
        boolean isUpStationExisted = hasStation(upStation);
        boolean isDownStationExisted = hasStation(downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public Optional<Section> findUpSection(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    public Optional<Section> findDownSection(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    public void remove(Section section) {
        sections.remove(section);
    }

    public boolean isRemovable() {
        return sections.size() > MIN_REMOVABLE_SIZE;
    }

    public void checkRemovable() {
        if (!isRemovable()) {
            throw new RuntimeException();
        }
    }

    public List<Station> getAllStations() {
        return sections.stream()
                .map(Section::getStations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public void checkConnected(Station source, Station target) {
        List<Station> stations = getAllStations();
        if (!stations.contains(source) || !stations.contains(target)) {
            throw new BusinessException(ErrorCode.STATION_NOT_CONNECTED);
        }
    }
    public int getMaxLineFare() {
        return sections.stream()
                .map(Section::getLineFare)
                .max(Integer::compare)
                .orElse(DEFAULT_LINE_FARE);
    }
}
