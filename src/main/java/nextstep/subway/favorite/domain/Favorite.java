package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Table(uniqueConstraints={@UniqueConstraint(columnNames={"member_id", "source_id", "target_id"})})
@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "source_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Station source;

    @JoinColumn(name = "target_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Station target;

    protected Favorite() {
    }

    public Favorite(Station source, Station target) {
        verifyAvailAble(source, target);
        this.source = source;
        this.target = target;
    }

    public Favorite(Member member, Station source, Station target) {
        this(source, target);
        verifyAvailAble(member);
        this.member = member;
        member.addFavorite(this);
    }

    public Favorite(Long id, Station source, Station target) {
        this(source, target);
        this.id = id;
    }

    public Favorite(Long id, Member member, Station source, Station target) {
        this(member, source, target);
        this.id = id;
    }

    private void verifyAvailAble(Station source, Station target) {
        if (Objects.isNull(source) || Objects.isNull(target)) {
            throw new IllegalArgumentException("즐겨찾기 생성에 필요한 역 정보가 부족합니다.");
        }
    }

    private void verifyAvailAble(Member member) {
        if (Objects.isNull(member)) {
            throw new IllegalArgumentException("즐겨찾기 생성에 필요한 사용자 정보가 부족합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(getMember(), favorite.getMember()) && Objects.equals(getSource(), favorite.getSource())
                && Objects.equals(getTarget(), favorite.getTarget());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMember(), getSource(), getTarget());
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
