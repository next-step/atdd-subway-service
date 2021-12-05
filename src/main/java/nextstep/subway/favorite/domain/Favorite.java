package nextstep.subway.favorite.domain;

import nextstep.subway.common.domain.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Station sourceStation;

    @ManyToOne
    private Station targetStation;

    @ManyToOne
    private Member member;

    public Long getId() {
        return id;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }

    protected Favorite() {
    }

    public Favorite(Station sourceStation, Station targetStation) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public void changeMember(Member member) {
        member.getFavorites().add(this);
        this.member = member;
    }

    public void validateOwner(Member member) {
        if (!isOwner(member)) {
            throw new NotOwnerException();
        }
    }

    private boolean isOwner(Member loginMember) {
        return member.equals(loginMember);
    }
}
