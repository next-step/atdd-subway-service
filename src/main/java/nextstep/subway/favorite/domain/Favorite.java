package nextstep.subway.favorite.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite {
    public static final String NOT_OWNER_EXCEPTION_MESSAGE = "본인의 즐겨찾기만 삭제할 수 있습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private Station target;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Favorite() {
    }

    public Favorite(Station source, Station target, Member member) {
        this.source = source;
        this.target = target;
        this.member = member;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public Long getId() {
        return id;
    }

    public void checkOwner(LoginMember loginMember) {
        if (!isOwner(loginMember)) {
            throw new IllegalArgumentException(NOT_OWNER_EXCEPTION_MESSAGE);
        }
    }

    private boolean isOwner(LoginMember loginMember) {
        return Objects.equals(member.getId(), loginMember.getId());
    }
}
