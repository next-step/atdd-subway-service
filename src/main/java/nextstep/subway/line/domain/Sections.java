package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Embeddable
public class Sections {

    private static final int HAS_ONE_SECTION = 1;
    private static final int FIRST_SECTION = 0;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    Station getUpStation() {
        Station downStation = sections.get(FIRST_SECTION).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findNextStationBackward(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }
        return downStation;
    }

    List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = getUpStation();

        while (downStation != null) {
            stations.add(downStation);
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findNextStationForward(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
        }
        return stations;
    }

    void addLineStation(Line line, Station upStation, Station downStation, Distance distance) {
        validateAdd(upStation, downStation);

        if (getStations().isEmpty()) {
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }
        if (isStationExisted(upStation)) {
            findNextStationForward(upStation).ifPresent(it -> it.updateUpStation(downStation, distance));
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }
        if (isStationExisted(downStation)) {
            findNextStationBackward(downStation).ifPresent(it -> it.updateDownStation(upStation, distance));
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }
        throw new SectionAddFailedException();
    }

    private void validateAdd(Station upStation, Station downStation) {
        if (isStationExisted(upStation) && isStationExisted(downStation)) {
            throw new SectionAddFailedException("이미 등록된 구간 입니다.");
        }
        if (!sections.isEmpty() && !isStationExisted(upStation) && !isStationExisted(downStation)) {
            throw new SectionAddFailedException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isStationExisted(Station upStation) {
        return getStations().stream().anyMatch(it -> it == upStation);
    }

    void removeLineStation(Station station) {
        if (sections.size() <= HAS_ONE_SECTION) {
            throw new SectionRemoveFailedException("구간을 제거할 수 없습니다.");
        }

        Optional<Section> upLineStation = findNextStationForward(station);
        Optional<Section> downLineStation = findNextStationBackward(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            Distance newDistance = upLineStation.get().getDistance().getMergedDistance(downLineStation.get().getDistance());
            sections.add(new Section(sections.get(FIRST_SECTION).getLine(), newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private Optional<Section> findNextStationForward(Station finalDownStation) {
        return sections.stream()
                .filter(it -> it.getUpStation() == finalDownStation)
                .findFirst();
    }

    private Optional<Section> findNextStationBackward(Station downStation) {
        return sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }
}
