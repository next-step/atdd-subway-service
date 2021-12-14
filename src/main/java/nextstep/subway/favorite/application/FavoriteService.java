package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
public class FavoriteService {

    private final StationService stationService;
    private final MemberService memberService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(final MemberService memberService, final StationService stationService, final FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public Favorite createFavorites(FavoriteRequest favoriteRequest, LoginMember loginMember) {

        Member member = memberService.findById(loginMember.getId());

        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());
        return favoriteRepository.save(Favorite.of(source.getId(), target.getId(), member.getId()));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAll(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(loginMember.getId());

        List<Long> stationIds = favorites.stream()
                .flatMap(f -> f.getStations().stream())
                .distinct()
                .collect(toList());

        List<Station> stations = stationService.findByIds(stationIds);
        return FavoriteResponse.ofList(favorites, stations);
    }

    @Transactional
    public void deleteFavorites(Long id, LoginMember loginMember) {
        favoriteRepository.deleteByIdAndMemberId(id, loginMember.getId());
    }
}
