package nextstep.subway.favorites.application;

import static nextstep.subway.member.application.MemberService.MEMBER_NOT_FOUND_ERROR;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorites.domain.Favorites;
import nextstep.subway.favorites.domain.FavoritesRepository;
import nextstep.subway.favorites.dto.FavoritesRequest;
import nextstep.subway.favorites.dto.FavoritesResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoritesService {

    public static final String FAVORITES_NOT_FOUND_ERROR = "요청에 해당하는 즐겨찾기를 찾을 수 없습니다.";
    private final FavoritesRepository favoritesRepository;
    private final StationService stationService;
    private final MemberRepository memberRepository;

    public FavoritesService(
        FavoritesRepository favoritesRepository,
        StationService stationService,
        MemberRepository memberRepository
    ) {
        this.favoritesRepository = favoritesRepository;
        this.stationService = stationService;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public FavoritesResponse saveFavorites(LoginMember loginMember, FavoritesRequest favoritesRequest) {
        Member member = memberRepository.findById(loginMember.getId())
            .orElseThrow(() -> new IllegalArgumentException(MEMBER_NOT_FOUND_ERROR));
        Station sourceStation = stationService.findStationById(favoritesRequest.getSource());
        Station targetStation = stationService.findStationById(favoritesRequest.getTarget());
        Favorites favorites = favoritesRequest.toEntity(member, sourceStation, targetStation);

        Favorites persistFavorites = favoritesRepository.save(favorites);
        return FavoritesResponse.from(persistFavorites);
    }

    public List<FavoritesResponse> findAllFavorites(LoginMember loginMember) {
        List<Favorites> persistFavorites = favoritesRepository.findAllByMemberId(loginMember.getId());
        return persistFavorites.stream()
            .map(FavoritesResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavoritesById(LoginMember loginMember, Long id) {
        Favorites favorites = findByIdAndMemberId(id, loginMember);
        favoritesRepository.deleteById(favorites.getId());
    }

    private Favorites findByIdAndMemberId(Long id, LoginMember loginMember) {
        return favoritesRepository.findByIdAndMemberId(id, loginMember.getId())
            .orElseThrow(() -> new IllegalArgumentException(FAVORITES_NOT_FOUND_ERROR));
    }
}
