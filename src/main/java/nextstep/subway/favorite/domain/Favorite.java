package nextstep.subway.favorite.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.exception.AuthorizationException;
import nextstep.subway.exception.CannotAddException;
import nextstep.subway.exception.CannotDeleteException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, optional = false)
    private Station source;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, optional = false)
    private Station target;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id")
    private Member owner;

    protected Favorite() {
    }

    private Favorite(Station source, Station target, Member owner) {
        this.source = source;
        this.target = target;
        this.owner = owner;
    }

    public static Favorite of(Station source, Station target, Member owner) {
        return new Favorite(source, target, owner);
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

    public boolean equalsFavorite(Favorite other) {
        return source.equalsName(other.source)
            && target.equalsName(other.target)
            && owner.equals(other.owner);
    }

    public void validateIncludeFavorite(List<Favorite> favorites) {
        if (includeFavorite(favorites)) {
            throw new CannotAddException("이미 등록된 즐겨찾기 입니다.");
        }
    }

    public void validateDelete(Member member) {
        if (!isOwner(member)) {
            throw new CannotDeleteException("다른사람의 즐겨찾기는 삭제할 수 없습니다.");
        }
    }

    protected boolean isOwner(Member member) {
        return this.owner.equals(member);
    }

    protected boolean includeFavorite(List<Favorite> favorites) {
        return favorites.stream()
            .filter(it -> equalsFavorite(it))
            .findFirst()
            .isPresent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Favorite other = (Favorite) o;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Favorite{" +
            "id=" + id +
            ", source=" + source +
            ", target=" + target +
            ", owner=" + owner +
            '}';
    }
}
