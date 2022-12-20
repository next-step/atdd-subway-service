package nextstep.subway.favorite.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_station_id", nullable = false)
    private Station sourceStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_station_id", nullable = false)
    private Station targetStation;

    public Favorite() {
    }

    private Favorite(Long memberId, Station sourceStation, Station targetStation) {
        this.memberId = memberId;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public static Favorite create(LoginMember loginMember, Station sourceStation, Station targetStation) {
        validateMemberExist(loginMember);
        return new Favorite(loginMember.getId(), sourceStation, targetStation);
    }

    private static void validateMemberExist(LoginMember loginMember) {
        loginMember.checkValidLoginMember();
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }
}
