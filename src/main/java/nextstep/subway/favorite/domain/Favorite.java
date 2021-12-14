package nextstep.subway.favorite.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Station source;

    @ManyToOne
    private Station target;

    @ManyToOne
    private Member owner;

    protected Favorite() {
    }

    public Favorite(final Station source, final Station target, final Member owner) {
        this.source = source;
        this.target = target;
        madeBy(owner);
    }

    public void madeBy(Member owner) {
        if (this.owner != null) {
            this.owner.getFavorites().remove(this);
        }
        this.owner = owner;
        owner.getFavorites().add(this);
    }

    public boolean isOwnedBy(final Long memberId) {
        return memberId.equals(owner.getId());
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

    public Member getOwner() {
        return owner;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Favorite favorite = (Favorite)obj;
        return id.equals(favorite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Favorite{" +
            "id=" + id +
            ", sourceId=" + source.getId() +
            ", targetId=" + target.getId() +
            ", ownerId=" + owner.getId() +
            '}';
    }
}
