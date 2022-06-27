package nextstep.subway.favorite.dto;

public class FavoriteRequest {
    private long source;
    private long target;

    private final long EMPTY_STATION = 0L;

    public FavoriteRequest(long source, long target) {
        valid(source, target);
        this.source = source;
        this.target = target;
    }

    void valid(long source, long target) {
        if (isEmptyStation(source, target)) {
            throw new IllegalArgumentException("즐겨찾기에 등록할 역은 필수 입니다.");
        }

    }

    private boolean isEmptyStation(long source, long target) {
        return source == EMPTY_STATION || target == EMPTY_STATION;
    }

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public Long getTarget() {
        return target;
    }

    public void setTarget(Long target) {
        this.target = target;
    }


}
