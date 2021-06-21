package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections implements Iterable<Section> {

    private static final int MIN_SECTIONS_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void add(Section section) {
        if (isEmpty()) {
            sections.add(section);
            return;
        }
        if (!contains(section.getUpStation()) && !contains(section.getDownStation())) {
            throw new IllegalArgumentException("구간 추가를 위해서는 기존 구간과의 연결점이 필요합니다. 역 정보를 확인해주세요.");
        }
        mergeByUpStation(section);
        mergeByDownStation(section);
        sections.add(section);
    }

    private void mergeByUpStation(Section section) {
        sections.stream()
                .filter(it -> it.getUpStation() == section.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void mergeByDownStation(Section section) {
        sections.stream()
                .filter(it -> it.getDownStation() == section.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    public void remove(Line line, Station station) {
        validateRemovable(station);

        Optional<Section> upStationMatching = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downStationMatching = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (upStationMatching.isPresent() && downStationMatching.isPresent()) {
            Station newUpStation = downStationMatching.get().getUpStation();
            Station newDownStation = upStationMatching.get().getDownStation();
            int newDistance = upStationMatching.get().getDistance() + downStationMatching.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upStationMatching.ifPresent(it -> sections.remove(it));
        downStationMatching.ifPresent(it -> sections.remove(it));
    }

    private void validateRemovable(Station station) {
        if (sections.size() <= MIN_SECTIONS_SIZE) {
            throw new IllegalStateException("구간의 갯수가 " + sections.size() + " 입니다. 삭제할 수 없습니다.");
        }
        if (!contains(station)) {
            throw new IllegalArgumentException("구간에 존재하지 않는 역입니다. 삭제하고자 하는 역 정보를 확인해 주세요.");
        }
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public boolean contains(Station station) {
        return stations().contains(station);
    }

    public List<Station> stations() {
        if (isEmpty()) {
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

    @Override
    public Iterator<Section> iterator() {
        return sections.iterator();
    }
}
