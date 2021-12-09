package nextstep.subway.favorite.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Embedded
    private MemberId memberId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "startStation_id", foreignKey = @ForeignKey(name = "pk_favorite_to_startStation"))
    public Station startStation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "endStation_id", foreignKey = @ForeignKey(name = "pk_favorite_to_endStation"))
    public Station endStation;

    public static Favorite of(Long memberId, Station startStation, Station endStation) {
        return new Favorite(memberId, startStation, endStation);
    }

    private Favorite(Long memberId, Station startStation, Station endStation) {
        this.memberId = new MemberId(memberId);
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public Favorite(Long id, Long memberId, Station startStation, Station endStation) {
        this(memberId, startStation, endStation);
        this.id = id;
    }

    protected Favorite() {
    }

    public Long getId() {
        return id;
    }

    public void canDeleted(MemberId memberId) {
        this.memberId.equalsId(memberId);
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }
}
