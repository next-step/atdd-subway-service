package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

import static nextstep.subway.line.domain.BizExceptionMessages.*;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextSection = sections.stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextSection.isPresent()) {
                break;
            }
            downStation = nextSection.get().getDownStation();
            stations.add(downStation);
        }

        return Collections.unmodifiableList(stations);
    }


    public void add(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException(SECTION_ALREADY_REGISTERED.message());
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
                stations.stream().noneMatch(it -> it == downStation)) {
            throw new IllegalStateException(SECTION_NOT_REACHABLE_ANY_STATION.message());
        }

        if (stations.isEmpty()) {
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }

        if (isUpStationExisted) {
            sections.stream()
                    .filter(it -> it.getUpStation() == upStation)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, distance));

            sections.add(new Section(line, upStation, downStation, distance));
        } else if (isDownStationExisted) {
            sections.stream()
                    .filter(it -> it.getDownStation() == downStation)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, distance));

            sections.add(new Section(line, upStation, downStation, distance));
        } else {
            throw new IllegalStateException();
        }
    }

    public void remove(Line line, Station station) {
        if (sections.size() <= 1) {
            throw new IllegalStateException(LINE_MIN_SECTIONS_SIZE.message());
        }
        // TODO : upstation, downstation 둘다 없으면?? --> 삭제할 역이 없음
        Optional<Section> preSection = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> nextSection = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (preSection.isPresent() && nextSection.isPresent()) {
            Station newUpStation = nextSection.get().getUpStation();
            Station newDownStation = preSection.get().getDownStation();
            int newDistance = preSection.get().getDistance() + nextSection.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        preSection.ifPresent(it -> sections.remove(it));
        nextSection.ifPresent(it -> sections.remove(it));
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextSection = sections.stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextSection.isPresent()) {
                break;
            }
            downStation = nextSection.get().getUpStation();
        }

        return downStation;
    }

    public List<Section> values() {
        return Collections.unmodifiableList(sections);
    }
}
