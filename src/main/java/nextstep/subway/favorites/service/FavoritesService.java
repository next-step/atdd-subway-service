package nextstep.subway.favorites.service;

import nextstep.subway.favorites.domain.Favorites;
import nextstep.subway.favorites.domain.FavoritesRepository;
import nextstep.subway.favorites.dto.FavoritesRequest;
import nextstep.subway.favorites.dto.FavoritesResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class FavoritesService {

    private final FavoritesRepository favoritesRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoritesService(FavoritesRepository favoritesRepository, StationService stationService, MemberService memberService) {
        this.favoritesRepository = favoritesRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public Favorites createFavorite(FavoritesRequest favoriteRequest, Long memberId) {
        Member member = memberService.findById(memberId);
        Station sourceStation = stationService.findStationById(favoriteRequest.getSource());
        Station targetStation = stationService.findStationById(favoriteRequest.getTarget());
        return favoritesRepository.save(new Favorites(sourceStation, targetStation, member));
    }

    @Transactional(readOnly = true)
    public List<FavoritesResponse> findAll(Long memberId) {
        return favoritesRepository.findAllByMember_Id(memberId).stream()
                .map(FavoritesResponse::of)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        favoritesRepository.deleteById(id);
    }
}
