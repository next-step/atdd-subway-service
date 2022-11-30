package nextstep.subway.favorite.domain;

import nextstep.subway.exception.FavoriteCreateException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.message.ExceptionMessage;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "source_station_id", nullable = false)
    private Station sourceStation;

    @ManyToOne
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
        checkStationNotNull(sourceStation);
        checkStationNotNull(targetStation);
        return new Favorite(member, sourceStation, targetStation);
    }

    private static void checkMemberNotNull(Member member) {
        if (member == null) {
            throw new FavoriteCreateException(ExceptionMessage.FAVORITE_NOT_HAVE_MEMBER);
        }
    }

    private static void checkStationNotNull(Station station) {
        if (station == null) {
            throw new FavoriteCreateException(ExceptionMessage.FAVORITE_NOT_HAVE_STATION);
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
