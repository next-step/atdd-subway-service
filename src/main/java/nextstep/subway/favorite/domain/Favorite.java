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
    private Member member;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_station")
    private Station sourceStation;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_station")
    private Station targetStation;

    protected Favorite(){}

    public Favorite(final Long id, final Member member, final Station sourceStation, final Station targetStation) {
        this.id = id;
        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public Favorite(final Member member, final Station sourceStation, final Station targetStation) {
        this(null, member, sourceStation, targetStation);
    }

    public static Favorite of(final Member member, final Station sourceStation, final Station targetStation) {
        return new Favorite(member, sourceStation, targetStation);
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "id=" + id +
                ", member=" + member +
                ", sourceStation=" + sourceStation +
                ", targetStation=" + targetStation +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Favorite favorite = (Favorite) o;
        return Objects.equals(id, favorite.id) && Objects.equals(member, favorite.member) && Objects.equals(sourceStation, favorite.sourceStation) && Objects.equals(targetStation, favorite.targetStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, sourceStation, targetStation);
    }
}
