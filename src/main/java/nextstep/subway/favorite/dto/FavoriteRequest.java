package nextstep.subway.favorite.dto;

public class FavoriteRequest {
    private Long source;
    private Long target;

    private FavoriteRequest() {
    }

    public FavoriteRequest(Long source, Long target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
        }
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
