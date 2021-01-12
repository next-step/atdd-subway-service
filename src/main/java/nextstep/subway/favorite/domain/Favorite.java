package nextstep.subway.favorite.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "source_id", nullable = false)
    private Station source;

    @ManyToOne
    @JoinColumn(name = "target_id", nullable = false)
    private Station target;

    @Builder
    public Favorite(Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
    }
}
