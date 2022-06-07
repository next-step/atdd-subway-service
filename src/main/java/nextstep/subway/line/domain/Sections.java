package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {
    private final static List<Station> CACHE = new ArrayList<>();

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> elements = new ArrayList<>();

    public void add(Section section) {
        validateAddition(section);

        elements.add(section);
    }

    private void validateAddition(Section section) {
        if (Objects.isNull(section)) {
            throw new IllegalArgumentException("구간이 없습니다.");
        }

        List<Station> stations = getStations();
        validateDuplicate(stations, section.getUpStation(), section.getDownStation());
        validateNoneMatch(stations, section.getUpStation(), section.getDownStation());
    }

    private void validateDuplicate(List<Station> stations, Station upStation, Station downStation) {
        boolean isUpStationExisted = stations.stream().anyMatch(station -> station.match(upStation));
        boolean isDownStationExisted = stations.stream().anyMatch(station -> station.match(downStation));

        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
    }

    private void validateNoneMatch(List<Station> stations, Station upStation, Station downStation) {
        if (stations.isEmpty()) {
            return;
        }

        boolean isUpStationNotExisted = stations.stream().noneMatch(station -> station.match(upStation));
        boolean isDownStationNotExisted = stations.stream().noneMatch(station -> station.match(downStation));

        if (isUpStationNotExisted && isDownStationNotExisted) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    public List<Section> getElements() {
        return Collections.unmodifiableList(elements);
    }

    public List<Station> getStations() {
        if (elements.isEmpty()) {
            return CACHE;
        }

        Stations stations = new Stations();
        stations.connectStation(getElements());

        return stations.getElements();
    }
}
