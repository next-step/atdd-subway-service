package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.FavoriteException;
import nextstep.subway.exception.error.ErrorCode;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoriteResponse findFavorite(Long id) {
        return FavoriteResponse.of(findOneNotEmpty(id));
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        return favoriteRepository.findAllByMemberId(loginMember.getId())
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberService.findOneMember(loginMember.getId());
        Station source = stationService.findStationById(request.getSource());
        Station target = stationService.findStationById(request.getTarget());

        return FavoriteResponse.of(favoriteRepository.save(new Favorite(member, source, target)));
    }

    private Favorite findOneNotEmpty(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(() -> new FavoriteException(ErrorCode.NOT_FOUND_ENTITY, "추가한 즐겨찾기가 없습니다."));
    }

    public void deleteFavorite(Long id) {
        Favorite favorite = findOneNotEmpty(id);
        favoriteRepository.delete(favorite);
    }

}
