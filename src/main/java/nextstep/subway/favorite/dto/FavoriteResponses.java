package nextstep.subway.favorite.dto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import nextstep.subway.favorite.domain.Favorite;

public class FavoriteResponses {
    private final List<FavoriteResponse> responses;

    private FavoriteResponses(List<FavoriteResponse> responses) {
        this.responses = responses;
    }

    public static FavoriteResponses from(List<Favorite> favorites) {
        List<FavoriteResponse> favoriteResponses = favorites.stream()
            .map(FavoriteResponse::from)
            .collect(Collectors.toList());
        return new FavoriteResponses(favoriteResponses);
    }

    public List<FavoriteResponse> getResponses() {
        return Collections.unmodifiableList(responses);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FavoriteResponses)) {
            return false;
        }
        FavoriteResponses that = (FavoriteResponses)o;
        return Objects.equals(responses, that.responses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responses);
    }
}
