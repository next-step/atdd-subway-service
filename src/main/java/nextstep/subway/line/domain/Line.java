package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private final Sections sections = new Sections();

    public Line() {
    }

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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Sections getSections() {
        return sections;
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void removeSection(Section section) {
        sections.remove(section);
    }

    public int getSectionsSize() {
        return sections.size();
    }

    public List<Station> getOrderedStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextSection = sections.getNextSectionByEqualUpStation(finalDownStation);
            if (!nextSection.isPresent()) {
                break;
            }
            downStation = nextSection.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextSection = sections.getNextSectionByEqualDownStation(finalDownStation);
            if (!nextSection.isPresent()) {
                break;
            }
            downStation = nextSection.get().getUpStation();
        }

        return downStation;
    }

    public void addLineStation(Station upStation, Station downStation, int distance) {
        boolean isUpStationExisted = sections.isStationExisted(upStation);
        boolean isDownStationExisted = sections.isStationExisted(downStation);

        raiseIfNotValidAddSection(isUpStationExisted, isDownStationExisted);

        if (sections.isEmpty()) {
            addSection(new Section(this, upStation, downStation, distance));
            return;
        }

        if (isUpStationExisted) {
            sections.getNextSectionByEqualUpStation(upStation)
                    .ifPresent(it -> it.updateUpStation(downStation, distance));

            addSection(new Section(this, upStation, downStation, distance));
            return;
        }

        if (isDownStationExisted) {
            sections.getNextSectionByEqualDownStation(downStation)
                    .ifPresent(it -> it.updateDownStation(upStation, distance));

            addSection(new Section(this, upStation, downStation, distance));
            return;
        }
        throw new RuntimeException();
    }

    private void raiseIfNotValidAddSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!sections.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }
}
