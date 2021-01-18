package nextstep.subway.member.dto;

import nextstep.subway.member.domain.Favorite;

import java.util.List;

public class FavoriteResponse {

    private List<Favorite> favorites;

    public FavoriteResponse() {
    }

    public FavoriteResponse(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

}
