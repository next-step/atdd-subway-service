package nextstep.subway.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    private static final String NOT_ALLOW_EQUALS_SOURCE_TARGET = "출발역과 도착역이 동일하여 등록할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private Station target;

    protected Favorite(Long id, Member member, Station source, Station target) {
        validate(source, target);
        this.id = id;
        this.member = member;
        this.source = source;
        this.target = target;
    }

    protected Favorite() {
    }
    
    public static Favorite of(Long id, Member member, Station source, Station target) {
        return new Favorite(id, member, source, target);
    }

    public static Favorite of(Member member, Station source, Station target) {
        return new Favorite(null, member, source, target);
    }

    private void validate(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(NOT_ALLOW_EQUALS_SOURCE_TARGET);
        }
    }

    public boolean isOwner(Long memberId) {
        return member.getId().equals(memberId);
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
