package nextstep.subway.line.domain;

import nextstep.subway.exception.ExistedSectionException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Embeddable
public class Sections {

    private static final int MIN_SECTION_COUNT = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Section> list() {
        return this.sections;
    }

    public void add(Section section) {
        this.sections.add(section);
    }

    public Optional<Section> findLineStation(Predicate<Section> express) {
        return this.sections.stream()
                .filter(express)
                .findFirst();
    }

    private List<Station> allStations() {
        List<Station> upStations = this.sections.stream()
                .map(Section::getUpStation)
                .collect(toList());

        List<Station> downStations = this.sections.stream()
                .map(Section::getDownStation)
                .collect(toList());

        upStations.addAll(downStations);

        return upStations.stream()
                .distinct()
                .collect(toList());
    }

    public void remove(Station station) {

        if (this.sections.size() <= MIN_SECTION_COUNT) {
            throw new IllegalArgumentException("구간이 1개일경우 제외할 수 없습니다.");
        }

        Optional<Section> upLineStation = findLineStation(it -> it.getUpStation() == station);
        Optional<Section> downLineStation = findLineStation(it -> it.getDownStation() == station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section downSection = downLineStation.get();
            Section upSection = upLineStation.get();
            this.sections.add(downSection.combine(upSection));
        }

        for(Section section : this.sections) {
            upLineStation.ifPresent(it -> System.out.println(it.equals(section)));
        }

        upLineStation.ifPresent(it -> this.sections.remove(it));
        downLineStation.ifPresent(it -> this.sections.remove(it));
    }

    public Station findUpStation() {
        Station upStation = this.sections.stream().findAny().get().getUpStation();
        return findUpStation(upStation);
    }

    public Station findUpStation(Station upStation) {

        Station finalDownStation = upStation;
        Optional<Section> nextLineStation = findLineStation(it -> it.getDownStation() == finalDownStation);

        if(nextLineStation.isPresent()) {
            upStation = nextLineStation.get().getUpStation();
        }
        return upStation;
    }

    public Station findDownStation(Station downStation) {
        Station finalDownStation = downStation;
        Optional<Section> nextLineStation = findLineStation(it -> it.getUpStation() == finalDownStation);

        if(nextLineStation.isPresent()) {
            downStation = nextLineStation.get().getDownStation();
        }
        return downStation;
    }

    private boolean matchAnyStation(List<Station> stations, Station findStation) {
        return stations.stream().anyMatch(station -> station == findStation);
    }

    private boolean matchNoneStation(List<Station> stations, Station findStation) {
        return !matchAnyStation(stations, findStation);
    }

    public void add(Line line, Station upStation, Station downStation, Integer distance) {
        List<Station> stations = allStations();

        boolean isUpStationExisted = matchAnyStation(stations, upStation);
        boolean isDownStationExisted = matchAnyStation(stations, downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new ExistedSectionException(upStation, downStation);
        }

        if (!sections.isEmpty() && matchNoneStation(stations, upStation) &&
                matchNoneStation(stations, downStation)) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }

        if(isUpStationExisted) {
            this.findLineStation(it -> it.getUpStation() == upStation)
                    .ifPresent(it -> it.updateUpStation(downStation, distance));
            this.add(new Section(line, upStation, downStation, distance));
            return;
        }

        this.findLineStation(it -> it.getDownStation() == downStation)
                .ifPresent(it -> it.updateDownStation(upStation, distance));
        this.sections.add(new Section(line, upStation, downStation, distance));
    }

    public boolean isEmpty() {
        return this.sections.isEmpty();
    }
}
