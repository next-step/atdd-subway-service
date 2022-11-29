package nextstep.subway.station.domain;

import java.util.NoSuchElementException;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;

@Entity
public class Station extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    protected Station() {
    }

    private Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Station from(String name) {
        return new Station(null, name);
    }

    public static Station of(Long id, String name) {
        return new Station(id, name);
    }

    public boolean hasNext(Sections sections) {
        return sections.findHasUpStation(this).isPresent();
    }

    public Station next(Sections sections) {
        Section section = sections.findHasUpStation(this).orElseThrow(NoSuchElementException::new);
        return section.getDownStation();
    }

    public boolean hasPrev(Sections sections) {
        return sections.findHasDownStation(this).isPresent();
    }

    public Station prev(Sections sections) {
        Section section = sections.findHasDownStation(this).orElseThrow(NoSuchElementException::new);
        return section.getUpStation();
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
        if (!(o instanceof Station)) {
            return false;
        }
        Station station = (Station)o;
        return Objects.equals(getId(), station.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
