package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    private Long sourceStationId;
    private Long targetStationId;

    public Favorite() {
    }

    public Favorite(Long member, Long source, Long target) {
        this.memberId = member;
        this.sourceStationId = source;
        this.targetStationId = target;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public boolean createdBy(Long memberId) {
        return this.memberId.equals(memberId);
    }
}
