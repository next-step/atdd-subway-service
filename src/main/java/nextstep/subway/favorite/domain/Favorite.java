package nextstep.subway.favorite.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_station_id")
    private Station sourceStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_station_id")
    private Station targetStation;

    protected Favorite() {
    }

    private Favorite(Member member, Station sourceStation, Station targetStation) {
        validFavorite(sourceStation, targetStation);
        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public static Favorite of(Member member, Station sourceStation, Station targetStation) {
        return new Favorite(member, sourceStation, targetStation);
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

    public void isOwner(Member member) {
        if (member != this.member) {
            throw new IllegalArgumentException("본인의 즐겨찾기만 삭제할 수 있습니다.");
        }
    }

    private static void validFavorite(Station sourceStation, Station targetStation) {
        if (sourceStation == targetStation) {
            throw new IllegalArgumentException("출발지와 목적지가 동일할 수 없습니다.");
        }
    }

}
