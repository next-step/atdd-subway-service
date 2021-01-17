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
    @JoinColumn(name = "member_id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Member member;
    @JoinColumn(name = "source_id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Station source;
    @JoinColumn(name = "target_id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Station target;

    public Favorite(Long id, Member member, Station source, Station target) {
        this.id = id;
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public Favorite(Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public static Favorite of(Member member, Station source, Station target) {
        return new Favorite(member, source, target);
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
