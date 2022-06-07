package nextstep.subway.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
@Table(name = "favorite")
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

    private Favorite(Station source, Station target, Member member) {
        this.source = source;
        this.target = target;
        this.member = member;
    }

    public static Favorite of(Station source, Station target, Member member) {
        return new Favorite(source, target, member);
    }

    public static Favorite createEmpty() {
        return new Favorite();
    }

    public Long getId() {
        return this.id;
    }

    public Station getSource() {
        return this.source;
    }

    public Station getTarget() {
        return this.target;
    }
}
