package nextstep.subway.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.favorite.exception.NotValidMemberException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @ManyToOne
    @JoinColumn(name = "source_id", nullable = false)
    private Station source;
    @ManyToOne
    @JoinColumn(name = "target_id", nullable = false)
    private Station target;

    public Favorite() {
    }

    public Favorite(Member member, Station source, Station target) {
        this(null, member, source, target);
    }

    public Favorite(Long id, Member member, Station source,
        Station target) {
        this.id = id;
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public static Favorite of(Member member, Station source, Station target) {
        return new Favorite(member, source, target);
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

    public void validateBeforeRemove(Long memberId) {
        if (!member.hasId(memberId)) {
            throw new NotValidMemberException("자신의 즐겨찾기가 아닌 항목은 삭제할 수 없습니다.");
        }
    }
}
