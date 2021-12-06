package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.function.Predicate;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Section> list() {
        return this.sections;
    }

    public void add(Section section) {
        this.sections.add(section);
    }

    private Station findStation(Map<Station, Station> relations, Station upStation) {

        Station station = relations.get(upStation);
        if(Objects.isNull(station)) {
            return upStation;
        }
        return findStation(relations, station);
    }

    private Map<Station, Station> relationDownToUpStation() {

        Map<Station, Station> relations = new HashMap<>();
        for(Section section : this.sections)
        {
            relations.put(section.getDownStation(), section.getUpStation());
        }
        return relations;
    }

    public Optional<Section> findLineStation(Predicate<Section> express) {
        return this.sections.stream()
                .filter(express)
                .findFirst();
    }

    public Station findUpStation() {
        final int random = 0;
        Station upStation = this.sections.get(random).getUpStation();
        Map<Station, Station> relations = relationDownToUpStation();
        return findStation(relations, upStation);
    }

    public void remove(Station station) {

        if (this.sections.size() <= 1) {
            throw new IllegalArgumentException("구간이 1개일경우 제외할 수 없습니다.");
        }

        Optional<Section> upLineStation = findLineStation(it -> it.getUpStation() == station);
        Optional<Section> downLineStation = findLineStation(it -> it.getDownStation() == station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section downSection = downLineStation.get();
            Section upSection = upLineStation.get();
            this.sections.add(downSection.combine(upSection));
        }

        upLineStation.ifPresent(it -> this.sections.remove(it));
        downLineStation.ifPresent(it -> this.sections.remove(it));
    }

    public Station findDownStation(Station downStation) {
        Station finalDownStation = downStation;
        Optional<Section> nextLineStation = findLineStation(it -> it.getUpStation() == finalDownStation);

        if(nextLineStation.isPresent()) {
            downStation = nextLineStation.get().getDownStation();
        }
        return downStation;
    }
}
