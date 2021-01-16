package nextstep.subway.member.domain;

import nextstep.subway.station.domain.Station;

import java.util.Objects;

public class Favorite {

    private Long id;

    private Station source;

    private Station target;

    public Favorite() {
    }

    public Favorite(Station source, Station target) {
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(id, favorite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "id=" + id +
                ", source=" + source +
                ", target=" + target +
                '}';
    }
}
