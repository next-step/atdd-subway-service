package nextstep.subway.favorites.dto;

import nextstep.subway.favorites.domain.Favorites;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FavoritesResponse {
    private final Long id;
    private final StationResponse source;
    private final StationResponse target;

    public FavoritesResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoritesResponse of(Favorites favorites) {
        return new FavoritesResponse(favorites.getId(),
                StationResponse.of(favorites.getUpStation()),
                StationResponse.of(favorites.getDownStation())
        );
    }

    public static List<FavoritesResponse> ofList(List<Favorites> favoritesList) {
        return favoritesList.stream()
                .map(FavoritesResponse::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoritesResponse response = (FavoritesResponse) o;
        return Objects.equals(id, response.id) && Objects.equals(source, response.source) && Objects.equals(target, response.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, target);
    }

    @Override
    public String toString() {
        return "FavoritesResponse{" +
                "id=" + id +
                ", source=" + source +
                ", target=" + target +
                '}';
    }
}
