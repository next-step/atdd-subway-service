package nextstep.subway.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "source_id")
    Station source;

    @ManyToOne
    @JoinColumn(name = "target_id")
    Station target;

    protected Favorite() {
    }

    private Favorite(Station source, Station target) {
        this.source = source;
        this.target = target;
    }

    public static Favorite of(Station source, Station target) {
        return new Favorite(source, target);
    }

    public Long getId() {
        return this.id;
    }

    public Station getSource() {
        return this.source;
    }

    public Station getTarget() {
        return this.target;
    }
}
