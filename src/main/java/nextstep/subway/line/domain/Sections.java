package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {

    private static final int MIN_SECTIONS_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void add(Section section) {
        validateAddable(section);
        if (isEmpty()) {
            sections.add(section);
            return;
        }
        divideByUpStation(section);
        divideByDownStation(section);
        sections.add(section);
    }

    private void validateAddable(Section section) {
        if (contains(section.getUpStation()) && contains(section.getDownStation())) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
        if (!isEmpty() && !contains(section.getUpStation()) && !contains(section.getDownStation())) {
            throw new IllegalArgumentException("구간 추가를 위해서는 기존 구간과의 연결점이 필요합니다. 역 정보를 확인해주세요.");
        }
    }

    private void divideByUpStation(Section section) {
        sections.stream()
                .filter(it -> it.getUpStation().equals(section.getUpStation()))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void divideByDownStation(Section section) {
        sections.stream()
                .filter(it -> it.getDownStation().equals(section.getDownStation()))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    public void remove(Line line, Station station) {
        validateRemovable(station);

        Optional<Section> upSection = findUpSection(station);
        Optional<Section> downSection = findDownSection(station);

        if (upSection.isPresent() && downSection.isPresent()) {
            Station newUpStation = upSection.get().getUpStation();
            Station newDownStation = downSection.get().getDownStation();
            int newDistance = upSection.get().getDistance() + downSection.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upSection.ifPresent(it -> sections.remove(it));
        downSection.ifPresent(it -> sections.remove(it));
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
        Station downStation = lastStopOfUpBound();
        stations.add(downStation);

        Optional<Section> downSection = findDownSection(downStation);
        while (downSection.isPresent()) {
            stations.add(downSection.get().getDownStation());
            downSection = findDownSection(downSection.get().getDownStation());
        }

        return stations;
    }

    private Station lastStopOfUpBound() {
        Station lastStop = sections.get(0).getUpStation();
        Optional<Section> upSection = findUpSection(lastStop);
        while (upSection.isPresent()) {
            lastStop = upSection.get().getUpStation();
            upSection = findUpSection(lastStop);
        }
        return lastStop;
    }

    private Optional<Section> findUpSection(Station station) {
        return sections.stream()
                .filter(section -> section.getDownStation().equals(station))
                .findFirst();
    }

    private Optional<Section> findDownSection(Station station) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(station))
                .findFirst();
    }
}
