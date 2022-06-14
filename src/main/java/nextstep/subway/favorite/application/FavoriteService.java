package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.AccessMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public FavoriteResponse createFavorite(AccessMember accessMember, Long sourceId, Long targetId) {
        Member member = memberService.findMemberById(accessMember.getId());
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);
        Favorite favorite = favoriteRepository.save(new Favorite(source, target, member));
        return FavoriteResponse.from(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(AccessMember accessMember) {
        Member member = memberService.findMemberById(accessMember.getId());
        List<Favorite> favorites = favoriteRepository.findAllByMember(member);
        return favorites.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long favoriteId) {
        try {
            favoriteRepository.deleteById(favoriteId);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("[ERROR] 즐겨찾기가 존재하지 않습니다.");
        }
    }

}
