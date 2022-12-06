package nextstep.subway.favorite.domain;

import nextstep.subway.exception.FavoriteCreateException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

import static nextstep.subway.utils.Message.FAVORITE_NOT_CONTAIN_MEMBER;
import static nextstep.subway.utils.Message.FAVORITE_NOT_CONTAIN_STATION;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne  // 단방향
    @JoinColumn(name = "source_station_id", nullable = false)
    private Station sourceStation;

    @ManyToOne  // 단방향
    @JoinColumn(name = "target_station_id", nullable = false)
    private Station targetStation;

    protected Favorite() {
    }

    private Favorite(Member member, Station sourceStation, Station targetStation) {
        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public static Favorite of(Member member, Station sourceStation, Station targetStation) {
        checkMemberNotNull(member);
        checkStationNotNull(sourceStation, targetStation);
        return new Favorite(member, sourceStation, targetStation);
    }

    private static void checkMemberNotNull(Member member) {
        if (member == null) {
            throw new FavoriteCreateException(FAVORITE_NOT_CONTAIN_MEMBER);
        }
    }

    private static void checkStationNotNull(Station sourceStation, Station targetStation) {
        if (sourceStation == null || targetStation == null) {
            throw new FavoriteCreateException(FAVORITE_NOT_CONTAIN_STATION);
        }
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
