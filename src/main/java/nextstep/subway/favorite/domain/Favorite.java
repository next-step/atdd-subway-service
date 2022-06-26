package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite extends BaseEntity {
    private static final String ERROR_MESSAGE_EQAUL_STATION = "출발역과 도착역이 동일할 수 없습니다.";
    private static final String ERROR_MESSAGE_NOT_OWNER = "즐겨찾기를 삭제할 권한이 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private Station target;

    protected Favorite() {
    }

    private Favorite(Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public static Favorite of(Member member, Station sourceStation, Station targetStation) {
        validate(sourceStation, targetStation);
        return new Favorite(member, sourceStation, targetStation);
    }

    private static void validate(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_EQAUL_STATION);
        }
    }

    public void vlidateOwner(Long memberId) {
        if (!Objects.equals(member.getId(), memberId)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_OWNER);
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
