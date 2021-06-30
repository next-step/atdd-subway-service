package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long sourceStationId;

    @Column(nullable = false)
    private Long targetStationId;

    protected Favorite() {
    }

    public Favorite(Long loginMemberId, Long source, Long target) {
        this(null, loginMemberId, source, target);
    }

    public Favorite(Long id, Long memberId, Long sourceStationId, Long targetStationId) {
        this.id = id;
        this.memberId = memberId;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Long getId() {
        return id;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
