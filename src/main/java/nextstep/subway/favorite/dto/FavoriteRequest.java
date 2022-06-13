package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.util.StringUtils;

public class FavoriteRequest {
    String source;
    String target;

    private FavoriteRequest(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public static FavoriteRequest of(Long source, Long target) {
        return new FavoriteRequest(String.valueOf(source), String.valueOf(target));
    }

    public long getSource() {
        return StringUtils.stringToLong(source).orElseThrow(() -> new IllegalStateException("출발역 ID가 유효한 값이 아닙니다."));
    }

    public long getTarget() {
        return StringUtils.stringToLong(target).orElseThrow(() -> new IllegalStateException("도착역 ID가 유효한 값이 아닙니다."));
    }
}
