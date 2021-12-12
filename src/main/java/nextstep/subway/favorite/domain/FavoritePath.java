package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class FavoritePath extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private Station target;

    protected FavoritePath() {
    }

    public FavoritePath(Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public static FavoritePath of(Member member, Station source, Station target) {
        return new FavoritePath(member, source, target);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
