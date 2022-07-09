package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.exception.SubwayExceptionMessage;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station target;

    public Favorite() {

    }

    public Favorite(Member member, Station source, Station target) {
        ensureNotNull(member, source, target);
        this.member = member;
        this.source = source;
        this.target = target;
    }

    private void ensureNotNull(Member member, Station source, Station target) {
        if (ObjectUtils.isEmpty(member) || ObjectUtils.isEmpty(source) || ObjectUtils.isEmpty(target)) {
            throw new IllegalArgumentException(SubwayExceptionMessage.INVALID_FAVORITE_INPUT.getMessage());
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

    public void isOwnedBy(Long memberId) {
        if (!member.getId().equals(memberId)) {
            throw new IllegalStateException(SubwayExceptionMessage.INVALID_FAVORITE_OWNER.getMessage());
        }
    }
}
