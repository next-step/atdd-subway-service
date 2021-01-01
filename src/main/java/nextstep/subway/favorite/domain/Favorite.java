package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.favorite.domain.excpetions.FavoriteCreationException;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames={"member_id", "source_id", "target_id"}))
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "target_id")
    private Long targetId;

    protected Favorite() {
    }

    Favorite(Long id, Long memberId, Long sourceId, Long targetId) {
        validate(memberId, sourceId, targetId);
        this.id = id;
        this.memberId = memberId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Favorite(Long memberId, Long sourceId, Long targetId) {
        this(null, memberId, sourceId, targetId);
    }

    public Long getId() {
        return id;
    }

    public List<Long> getStations() {
        return Arrays.asList(sourceId, targetId);
    }

    private void validate(Long memberId, Long sourceId, Long targetId) {
        if (memberId == null || sourceId == null || targetId == null) {
            throw new FavoriteCreationException("회원, 출발지, 도착지가 모두 필요합니다.");
        }
    }
}
