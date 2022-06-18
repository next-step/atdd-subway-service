package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.consts.ErrorMessage;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
@Table(
        uniqueConstraints={
                @UniqueConstraint(
                        name = "uniqueMemberSourceTarget",
                        columnNames = {"member_id", "source_id", "target_id"}
                )
        }
)
public class Favorite extends BaseEntity {
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

    protected Favorite() {
    }

    public Favorite(Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public static Favorite of(Member member, Station sourceStation, Station targetStation) {
        validateSourceAndTarget(sourceStation, targetStation);
        return new Favorite(member, sourceStation, targetStation);
    }

    private static void validateSourceAndTarget(Station sourceStation, Station targetStation) {
        if(sourceStation.equals(targetStation)){
            throw new IllegalArgumentException(ErrorMessage.ERROR_FAVORITE_SAME_SOURCE_TARGET);
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
