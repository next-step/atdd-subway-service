package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private StationService stationService;
    private FavoriteRepository favoriteRepository;
    private MemberService memberService;

    public FavoriteService(StationService stationService, FavoriteRepository favoriteRepository, MemberService memberService) {
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
    }

    public FavoriteResponse create(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Station sourceStation = stationService.findById(Long.valueOf(favoriteRequest.getSource()));
        Station targetStation = stationService.findById(Long.valueOf(favoriteRequest.getTarget()));

        Member member =  memberService.findMemberEntity(loginMember.getId());

        Favorite createdFavorite = favoriteRepository.save(Favorite.of(sourceStation, targetStation, member));
        
        return FavoriteResponse.of(createdFavorite);
    }

    public List<FavoriteResponse> findByMember(LoginMember loginMember) {
        Member member =  memberService.findMemberEntity(loginMember.getId());

        return favoriteRepository.findByMember(member).stream()
                                                        .map(FavoriteResponse::of)
                                                        .collect(Collectors.toList());
    }

    public void deleteById(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }
}
