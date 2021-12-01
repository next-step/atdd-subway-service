package nextstep.subway.favorite.domain;

import nextstep.subway.common.domain.BaseEntity;
import nextstep.subway.member.domain.Member;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sourceStationId;

    private Long targetStationId;

    @ManyToOne
    private Member member;

    public Long getId() {
        return id;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    protected Favorite() {
    }

    public Favorite(Long sourceStationId, Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public void changeMember(Member member) {
        member.getFavorites().add(this);
        this.member = member;
    }
}
