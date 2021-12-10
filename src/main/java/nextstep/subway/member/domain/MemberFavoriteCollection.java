package nextstep.subway.member.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.member.application.exception.FavoriteErrorCode;
import nextstep.subway.member.dto.favorite.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class MemberFavoriteCollection {

    private final Member member;
    private final List<Station> stations;

    public MemberFavoriteCollection(Member member, List<Station> stations) {
        this.member = member;
        this.stations = stations;
    }

    public static MemberFavoriteCollection of(Member member, List<Station> stations) {
        return new MemberFavoriteCollection(member, stations);
    }

    public List<FavoriteResponse> toFavoriteResponses() {
        List<FavoriteResponse> result = new ArrayList<>();
        for (Favorite favorite : member.getFavorites()) {
            StationResponse source = getStationResponseById(favorite.getStationSourceId());
            StationResponse target = getStationResponseById(favorite.getStationTargetId());

            FavoriteResponse favoriteResponse = new FavoriteResponse(favorite.getId(), source,
                target);
            result.add(favoriteResponse);
        }

        return result;
    }

    private StationResponse getStationResponseById(Long stationId) {
        return StationResponse.of(stations.stream()
            .filter(station -> Objects.equals(station.getId(), stationId))
            .findFirst()
            .orElseThrow(
                () -> InvalidParameterException.of(FavoriteErrorCode.FAVORITE_STATION_NOT_FOUND)));
    }
}
