package nextstep.subway.favorite.domain;

import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_id")
    private Station source;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_id")
    private Station target;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Favorite() {
    }

    public static Favorite of(Member member, Station source, Station target){
        return new Favorite(member, source, target);
    }

    private Favorite( Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Favorite)) {
            return false;
        }
        Favorite favorite = (Favorite) o;
        return Objects.equals(getId(), favorite.getId()) && Objects.equals(getSource(),
                favorite.getSource()) && Objects.equals(getTarget(), favorite.getTarget())
                && Objects.equals(getMember(), favorite.getMember());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSource(), getTarget(), getMember());
    }
}