package nextstep.subway.favorite.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.common.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import javax.persistence.*;
import java.util.Objects;

@Getter @NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "unique_favorite", columnNames={"member_id", "source", "target"}))
public class Favorite extends BaseEntity {

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_favorite_to_member"))
    private Member member;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "source", foreignKey = @ForeignKey(name = "fk_favorite_to_source"))
    private Station source;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "target", foreignKey = @ForeignKey(name = "fk_favorite_to_target"))
    private Station target;

    @Builder
    public Favorite(final Member member, final Station source, final Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public boolean isOwner(final Long memberId) {
        return Objects.equals(member.getId(), memberId);
    }
}
