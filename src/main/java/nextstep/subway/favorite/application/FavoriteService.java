package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           MemberService memberService,
                           StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, Long sourceId, Long targetId){
        Member member = memberService.findMemberById(loginMember.getId());
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);
        validateSameStation(source, target);
        Favorite favorite = favoriteRepository.save(new Favorite(source, target, member));
        return FavoriteResponse.from(favorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember){
        Member member = memberService.findMemberById(loginMember.getId());
        List<Favorite> favorites = favoriteRepository.findAllByMember(member);
        return favorites.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long favoriteId){
        try {
            favoriteRepository.deleteById(favoriteId);
        }catch (EmptyResultDataAccessException e){
            throw new IllegalArgumentException("[ERROR] 즐겨찾기가 존재하지 않습니다.");
        }
    }

    private void validateSameStation(Station source, Station target) {
        if(source.equals(target)){
            throw new IllegalArgumentException("[ERROR] 동일한 역을 즐겨찾기로 등록할 수 없습니다.");
        }
    }

}
