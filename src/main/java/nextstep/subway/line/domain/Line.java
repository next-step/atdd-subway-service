package nextstep.subway.line.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.common.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "unique_line_name", columnNames={"name"}))
public class Line extends BaseEntity {

    private String name;

    private String color;

    @Embedded
    private Sections sections = new Sections();

    public Line(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public Line(final String name, final String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;

        Section section = Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();

        section.registerLine(this);
    }

    public void addSection(final Section section) {
        sections.add(section);
    }

    public void update(final Line line) {
        this.name = line.name;
        this.color = line.color;
        this.sections.update(line.getSections());
    }

    public List<Station> getStations() {
        return sections.sortedStations();
    }

    public List<Integer> getDistances() {
        return sections.sortedSections()
                .stream()
                .map(x -> x.getDistance())
                .collect(Collectors.toList());
    }

}
