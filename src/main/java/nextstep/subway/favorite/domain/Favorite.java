package nextstep.subway.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite extends BaseEntity {

    private static final String ERROR_MESSAGE_SOURCE_NULL = "출발역의 값이 null 입니다.";
    private static final String ERROR_MESSAGE_TARGET_NULL = "도착역의 값이 null 입니다.";
    private static final String ERROR_MESSAGE_MEMBER_NULL = "생성 멤버의 값이 null 입니다.";
    private static final String ERROR_MESSAGE_SAME_STATION = "출발역과 도착역이 같습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_station_id")
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_station_id")
    private Station target;

    private Long memberId;

    protected Favorite() {
    }

    public Favorite(Station source, Station target, Long memberId) {
        validate(source, target, memberId);

        this.source = source;
        this.target = target;
        this.memberId = memberId;
    }

    private void validate(Station source, Station target, Long memberId) {
        validateNull(source, ERROR_MESSAGE_SOURCE_NULL);
        validateNull(target, ERROR_MESSAGE_TARGET_NULL);
        validateNull(memberId, ERROR_MESSAGE_MEMBER_NULL);

        validateDuplicatedStation(source, target);
    }

    private void validateNull(Object object, String errorMessage) {
        if (object == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void validateDuplicatedStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_SAME_STATION);
        }
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

    public Long getMemberId() {
        return memberId;
    }

    public boolean isOwner(Long memberId) {
        return this.memberId.equals(memberId);
    }

    public boolean isSameRoute(Favorite favorite) {
        return this.source.equals(favorite.getSource()) && this.target.equals(favorite.getTarget());
    }
}
