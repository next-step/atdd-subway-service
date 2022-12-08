package nextstep.subway.favorites.application;

import nextstep.subway.ErrorMessage;
import nextstep.subway.favorites.domain.Favorites;
import nextstep.subway.favorites.domain.FavoritesRepository;
import nextstep.subway.favorites.dto.FavoritesRequest;
import nextstep.subway.favorites.dto.FavoritesResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.station.application.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoritesService {

    private final FavoritesRepository favoritesRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoritesService(FavoritesRepository favoritesRepository, StationService stationService, MemberService memberService) {
        this.favoritesRepository = favoritesRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    @Transactional
    public FavoritesResponse createFavorites(Long memberId, FavoritesRequest param) {
        Favorites favorites = favoritesRepository.save(new Favorites(stationService.findStationById(param.getSourceStationId()),
                stationService.findStationById(param.getTargetStationId()),
                memberService.findMember(memberId)));
        return FavoritesResponse.of(favorites);
    }

    public List<FavoritesResponse> retrieveFavoritesList(Long memberId) {
        List<Favorites> favorites = favoritesRepository.findByMember(memberService.findMember(memberId))
                .orElse(Arrays.asList());
        return favorites.stream().map(FavoritesResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMember(Long memberId, Long favoritesId) {
        List<Favorites> favoritesList = favoritesRepository.findByMember(memberService.findMember(memberId))
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.DO_NOT_EXIST_FAVORITES_LIST.getMessage()));
        if(isNotFavorites(favoritesList, favoritesId)) {
            throw new IllegalArgumentException(ErrorMessage.DO_NOT_EXIST_FAVORITES_ID.getMessage());
        }
        favoritesRepository.deleteById(favoritesId);
    }

    private boolean isNotFavorites(List<Favorites> favoritesList, Long favoritesId) {
        return favoritesList.stream().map(Favorites::getId).noneMatch(favoritesId::equals);
    }
}
