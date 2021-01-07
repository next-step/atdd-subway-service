package nextstep.subway.line.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.BaseEntity;
import nextstep.subway.line.application.AddLineException;
import nextstep.subway.line.application.RemoveLineException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@NoArgsConstructor
@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addLineStation(Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.contains(upStation);
        boolean isDownStationExisted = stations.contains(downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new AddLineException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new AddLineException("등록할 수 없는 구간 입니다.");
        }

        if (isUpStationExisted || isDownStationExisted) {
            for (Section section : sections) {
                AddSectionType addSectionType = AddSectionType.valueOf(section, upStation, downStation);
                if (addSectionType != AddSectionType.NONE) {
                    addSectionType.updateStation(section, upStation, downStation, distance);
                    break;
                }
            }
        }

        sections.add(new Section(this, upStation, downStation, distance));
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findSection(section -> section.getUpStation() == finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    public void removeLineStation(Station station) {
        if (sections.size() <= 1) {
            throw new RemoveLineException("Cannot delete a single section");
        }

        Optional<Section> upLineStation = findSection(section -> section.getUpStation() == station);
        Optional<Section> downLineStation = findSection(section -> section.getDownStation() == station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            sections.add(Section.combine(upLineStation.get(), downLineStation.get()));
        }

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findSection(section -> section.getDownStation() == finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    private Optional<Section> findSection(Predicate<Section> sectionPredicate) {
        return sections.stream()
                .filter(sectionPredicate)
                .findFirst();
    }
}
