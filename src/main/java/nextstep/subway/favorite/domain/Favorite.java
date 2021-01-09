package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {
    private static final String ERR_TEXT_INVALID_MEMBER = "멤버 데이터가 올바르지 않습니다.";
    private static final String ERR_TEXT_INVALID_STATIONS = "지하철 역 데이터가 올바르지 않습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private Station target;

    protected Favorite() {
    }

    protected Favorite(final Member member, final Station source, final Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public static Favorite of(final Member member, final Station source, final Station target) {
        if (member == null) {
            throw new IllegalArgumentException(ERR_TEXT_INVALID_MEMBER);
        }

        if (source == null || target == null || source == target) {
            throw new IllegalArgumentException(ERR_TEXT_INVALID_STATIONS);
        }

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
}
