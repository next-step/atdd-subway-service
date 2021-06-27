package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Table(name = "favorite")
@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "source_station_id")
    private Station source;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "target_station_id")
    private Station target;

    protected Favorite() {
    }

    private Favorite(Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public static Favorite of(Member member, Station source, Station target) {
        validateNotEqualsSourceAndTarget(source, target);
        return new Favorite(member, source, target);
    }

    private static void validateNotEqualsSourceAndTarget(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("동일한 역으로 즐겨찾기를 추가할 수 없습니다.");
        }
    }

    public void checkSameMember(Member member) {
        if (!this.member.equals(member)) {
            throw new IllegalArgumentException("즐겨찾기를 추가한 사용자가 아닙니다.");
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(id, favorite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
