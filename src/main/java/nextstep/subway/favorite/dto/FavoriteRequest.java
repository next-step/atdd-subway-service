package nextstep.subway.favorite.dto;

public class FavoriteRequest {
    private Long id;
    private final Long memberId;
    private Long source;
    private Long target;

    public FavoriteRequest(Long memberId) {
        this.memberId = memberId;
    }

    public FavoriteRequest(Long memberId, Long source, Long target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
