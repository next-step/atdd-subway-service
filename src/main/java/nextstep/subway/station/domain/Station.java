package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import nextstep.subway.BaseEntity;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;

@Entity
public class Station extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "upStation")
    private List<Section> sectionsByUpStation = new ArrayList<>();

    @OneToMany(mappedBy = "downStation")
    private List<Section> sectionsByDownStation = new ArrayList<>();

    public Station() {
    }

    public Station(String name) {
        this.name = name;
    }

    public Set<Line> getLinesInSections() {
        Set<Line> lines = new HashSet<>();

        sectionsByUpStation.forEach(section -> lines.add(section.getLine()));
        sectionsByDownStation.forEach(section -> lines.add(section.getLine()));

        return lines;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return Objects.equals(id, station.id) &&
                Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
