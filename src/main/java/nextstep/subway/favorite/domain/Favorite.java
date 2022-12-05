package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite extends BaseEntity {

    public static final String TARGET_SOURCE_SAME_EXCEPTION_MESSAGE = "출발역과 도착역이 같을 수 없다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "target_id", nullable = false)
    private Station target;

    @ManyToOne
    @JoinColumn(name = "source_id", nullable = false)
    private Station source;

    protected Favorite() {
    }

    public Favorite(Member member, Station source, Station target) {
        validate(member, source, target);
        this.member = member;
        this.source = source;
        this.target = target;
    }

    private void validate(Member member, Station source, Station target) {
        if (Objects.isNull(member)) {
            throw new IllegalArgumentException();
        }
        if (Objects.isNull(source)) {
            throw new IllegalArgumentException();
        }
        if (Objects.isNull(target)) {
            throw new IllegalArgumentException();
        }
        validateStation(target, source);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(id, favorite.id) && Objects.equals(target, favorite.target) && Objects.equals(source, favorite.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, target, source);
    }

    private void validateStation(Station target, Station source) {
        if (target.equals(source)) {
            throw new IllegalArgumentException(TARGET_SOURCE_SAME_EXCEPTION_MESSAGE);
        }
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

    public boolean isSame(Favorite favorite) {
        return this.getSource().equals(favorite.getSource()) && this.target.equals(favorite.getTarget());
    }
}
