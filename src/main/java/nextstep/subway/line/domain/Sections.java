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
        if (Objects.isNull(section)) {
            return;
        }
        validateDuplicate(section.getUpStation(), section.getDownStation());
        elements.add(section);
    }

    private void validateDuplicate(Station upStation, Station downStation) {
        List<Station> stations = getStations();

        boolean isUpStationExisted = stations.stream().anyMatch(station -> station.match(upStation));
        boolean isDownStationExisted = stations.stream().anyMatch(station -> station.match(downStation));

        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
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
