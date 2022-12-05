package nextstep.subway.favorite.dto;

import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;

public class FavoriteStationResponse {

    private Long id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public FavoriteStationResponse() {}

    public FavoriteStationResponse(Station station) {
        this.id = station.getId();
        this.name = station.getName();
        this.createdDate = station.getCreatedDate();
        this.modifiedDate = station.getModifiedDate();
    }

    public static FavoriteStationResponse of(Station station) {
        return new FavoriteStationResponse(station);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
