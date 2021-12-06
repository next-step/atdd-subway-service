package nextstep.subway.favorites.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.ServiceException;
import nextstep.subway.favorites.domain.Favorites;
import nextstep.subway.favorites.domain.FavoritesRepository;
import nextstep.subway.favorites.dto.FavoritesRequest;
import nextstep.subway.favorites.dto.FavoritesResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritesService {
    private final FavoritesRepository favoritesRepository;
    private final StationService stationService;
    private final MemberRepository memberRepository;

    public FavoritesService(FavoritesRepository favoritesRepository, StationService stationService, MemberRepository memberRepository) {
        this.favoritesRepository = favoritesRepository;
        this.stationService = stationService;
        this.memberRepository = memberRepository;
    }

    public FavoritesResponse createFavorites(LoginMember loginMember, FavoritesRequest favoritesRequest) {
        Station upStation = stationService.findStationById(favoritesRequest.getSource());
        Station downStation = stationService.findStationById(favoritesRequest.getTarget());
        Member owner = memberRepository.findById(loginMember.getId()).orElseThrow(() -> new ServiceException("사용자를 찾을 수 없습니다"));
        Favorites persistFavorites = favoritesRepository.save(new Favorites(owner, upStation, downStation));
        return FavoritesResponse.of(persistFavorites);
    }

    public List<FavoritesResponse> getAll(LoginMember loginMember) {
        Member owner = memberRepository.findById(loginMember.getId()).orElseThrow(() -> new ServiceException("사용자를 찾을 수 없습니다"));
        return FavoritesResponse.ofList(favoritesRepository.findByOwner(owner));
    }
}
