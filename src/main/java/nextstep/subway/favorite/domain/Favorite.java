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

import static java.util.Objects.isNull;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private Member member;

    @ManyToOne(optional = false)
    @JoinColumn(name = "source_id", nullable = false)
    private Station source;

    @ManyToOne(optional = false)
    @JoinColumn(name = "target_id", nullable = false)
    private Station target;

    protected Favorite() {
    }

    private Favorite(Member member, Station source, Station target) {
        validate(member, source, target);
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public static Favorite of(Member member, Station source, Station target) {
        return new Favorite(member, source, target);
    }

    private void validate(Member member, Station source, Station target) {
        if (isNull(member)) {
            throw new IllegalArgumentException("즐겨찾기할 유저가 존재하지 않습니다.");
        }
        if (isNull(source)) {
            throw new IllegalArgumentException("즐겨찾기할 출발역이 존재하지 않습니다.");
        }
        if (isNull(target)) {
            throw new IllegalArgumentException("즐겨찾기할 도착역이 존재하지 않습니다.");
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
}
