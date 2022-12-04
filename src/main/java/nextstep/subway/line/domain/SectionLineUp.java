package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationLineUp;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Embeddable
public class SectionLineUp {

    private static final int FIRST_INDEX = 0;

    @OneToMany(mappedBy = "line", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public SectionLineUp(List<Section> sections) {
        this.sections = sections;
    }

    protected SectionLineUp() {

    }

    public void add(Section section) {
        StationLineUp stationLineUp = new StationLineUp(getStations());
        if (stationLineUp.isEmpty()) {
            this.sections.add(section);
            return;
        }
        validAlreadyExistSection(stationLineUp, section);
        validNotExistSection(stationLineUp, section);

        addSection(stationLineUp, section);
    }

    private void addSection(StationLineUp stationLineUp, Section section) {

        if (isEndStation(section)) {
            this.sections.add(section);
            return;
        }
        if (stationLineUp.stationExisted(section.getUpStation())) {
            updateUpStation(section);
            this.sections.add(section);
            return;
        }
        updateDownStation(section);
        this.sections.add(section);
    }

    private void updateDownStation(Section section) {
        this.sections.stream()
                .filter(it -> section.isSameDownSection(it.getUpStation()))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    private void updateUpStation(Section section) {
        this.sections.stream()
                .filter(it -> section.isSameUpSection(it.getUpStation()))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private boolean isEndStation(Section section) {
        return isEndUpSection(section) || isEndDownSection(section);
    }

    private boolean isEndUpSection(Section section) {
        return section.isSameDownSection(findUpStation(sections.get(FIRST_INDEX).getUpStation()));
    }

    private boolean isEndDownSection(Section section) {
        return section.isSameDownSection(findDownStation(sections.get(FIRST_INDEX).getDownStation()));
    }

    private void validAlreadyExistSection(StationLineUp stationLineUp, Section section) {
        if (stationLineUp.stationExisted(section.getUpStation()) &&
                stationLineUp.stationExisted(section.getDownStation())) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
    }

    private void validNotExistSection(StationLineUp stationLineUp, Section section) {
        if (!stationLineUp.isEmpty() && stationLineUp.unKnownStation(section.getUpStation())
                && stationLineUp.unKnownStation(section.getDownStation())) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }
        final Set<Station> stations = new LinkedHashSet<>();
        Station firstStation = findUpStation(sections.get(FIRST_INDEX).getUpStation());
        addStations(stations, firstStation);
        return new ArrayList<>(stations);
    }

    private void addStations(Set<Station> stations, Station firstStation) {
        sections.stream().filter(it -> it.isSameUpSection(firstStation))
                .findFirst()
                .ifPresent(it -> {
                    stations.add(it.getUpStation());
                    stations.add(it.getDownStation());
                    addStations(stations, it.getDownStation());
                });
    }

    private Station findUpStation(Station station) {
        return sections.stream().filter(it -> it.isSameDownSection(station))
                .findFirst()
                .map(it -> findUpStation(it.getUpStation()))
                .orElse(station);
    }

    private Station findDownStation(Station station) {
        return sections.stream().filter(it -> it.isSameUpSection(station))
                .findFirst()
                .map(it -> findDownStation(it.getDownStation()))
                .orElse(station);
    }

    public void remove(Station station) {
        if (this.sections.size() <= 1) {
            throw new IllegalStateException("구간 삭제는 구간이 2개 이상일 경우 가능합니다");
        }

        Optional<Section> upLineStation = this.sections.stream()
                .filter(it -> it.getUpStation().equals(station))
                .findFirst();
        Optional<Section> downLineStation = this.sections.stream()
                .filter(it -> it.getDownStation().equals(station))
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            Distance newDistance = upLineStation.get().getDistance().plus(downLineStation.get().getDistance());
            this.sections.add(
                    new Section(sections.get(FIRST_INDEX).getLine(), newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    public int sectionSize() {
        return this.sections.size();
    }
}
