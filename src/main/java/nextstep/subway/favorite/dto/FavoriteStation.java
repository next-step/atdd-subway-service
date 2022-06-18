package nextstep.subway.favorite.dto;

import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;

public class FavoriteStation {
    private Long id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDAte;

    private FavoriteStation() {
    }

    private FavoriteStation(Long id, String name, LocalDateTime createdDate, LocalDateTime modifiedDAte) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.modifiedDAte = modifiedDAte;
    }

    public static FavoriteStation of(Station station) {
        return new FavoriteStation(
                station.getId(), station.getName(), station.getCreatedDate(), station.getModifiedDate());
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

    public LocalDateTime getModifiedDAte() {
        return modifiedDAte;
    }
}
