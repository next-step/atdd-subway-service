package nextstep.subway.favorite.domain.adapters;

import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;
import java.util.Objects;

public class SafeStationInFavorite {
    private final Long id;
    private final String name;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    SafeStationInFavorite(final Long id, final String name, final LocalDateTime createdDate, final LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public SafeStationInFavorite(Station station) {
        this(station.getId(), station.getName(), station.getCreatedDate(), station.getModifiedDate());
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SafeStationInFavorite that = (SafeStationInFavorite) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(createdDate, that.createdDate) && Objects.equals(modifiedDate, that.modifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createdDate, modifiedDate);
    }
}
