package nextstep.subway.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_favorite_to_member"), nullable = false)
    private Member member;
    @ManyToOne
    @JoinColumn(name = "source_station_id", foreignKey = @ForeignKey(name = "fk_favorite_to_sourcestation"), nullable = false)
    private Station sourceStation;
    @ManyToOne
    @JoinColumn(name = "target_station_id", foreignKey = @ForeignKey(name = "fk_favorite_to_targetstation"), nullable = false)
    private Station targetStation;

    protected Favorite() {
    }

    public Favorite(Member member, Station sourceStation, Station targetStation) {
        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public Long getId() {
        return id;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }
}
