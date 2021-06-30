package nextstep.subway.favorite.dto;

import java.util.Objects;

public class FavoriteRequest {
    private static final String STATION_NOT_FOUND = "역이 입력되지 않았습니다.";
    private static final String STATIONS_DUPLICATED = "동일역을 등록할 수 없습니다";

    private Long source;
    private Long target;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long source, Long target) {
        validateStations(source, target);
        this.source = source;
        this.target = target;
    }

    private void validateStations(Long source, Long target) {
        if (Objects.isNull(source) || Objects.isNull(target)) {
            throw new IllegalArgumentException(STATION_NOT_FOUND);
        }
        if (Objects.equals(source, target)) {
            throw new IllegalArgumentException(STATION_NOT_FOUND);
        }
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
