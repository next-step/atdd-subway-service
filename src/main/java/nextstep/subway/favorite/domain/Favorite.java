package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private Station target;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    protected Favorite() {}

    public Favorite(Station source, Station target, Member member) {
        this.source = source;
        this.target = target;
        this.member = member;
    }

    public static Favorite of(Station sourceStation, Station targetStation, Member member) {
        return new Favorite(sourceStation, targetStation, member);
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

    public Member getMember() {
        return member;
    }
}
