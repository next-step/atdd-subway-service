package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

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

    public Favorite() {
    }

    public Favorite(Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
        member.addFavorite(this);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(id, favorite.id) && Objects.equals(source, favorite.source) && Objects.equals(target, favorite.target) && Objects.equals(member, favorite.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, target, member);
    }
}
