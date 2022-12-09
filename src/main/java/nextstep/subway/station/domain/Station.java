package nextstep.subway.station.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Station extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    public Station() {}

    public Station(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void validationSaveFavoriteRequest(Station targetStation, List<Line> lines) {
        PathFinder pathFinder = new PathFinder();
        pathFinder.findFastPaths(lines, this, targetStation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(id, station.id) &&
                Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
