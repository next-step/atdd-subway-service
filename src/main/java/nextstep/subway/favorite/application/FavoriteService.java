package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.advice.exception.FavoriteBadRequestException;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(final MemberService memberService, final StationService stationService, final FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public List<FavoriteResponse> findFavorites(Long id) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(id);

        return favorites
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberService.getMember(loginMember.getId());
        Station sourceStation = stationService.findById(request.getSourceStationId());
        Station targetStation = stationService.findById(request.getTargetStationId());
        Favorite favorite = new Favorite(member, sourceStation, targetStation);

        return FavoriteResponse.of(favoriteRepository.save(favorite));
    }

    @Transactional
    public void deleteFavorite(LoginMember loginMember, Long id) {
        Favorite favorite = getFavorite(id);

        if (!favorite.isOwner(loginMember.getId())) {
            new FavoriteBadRequestException("삭제할 권한이 없습니다.", loginMember.getId());
        }

        favoriteRepository.delete(favorite);
    }

    private Favorite getFavorite(Long id) {
        return favoriteRepository.findById(id).orElseThrow(() -> new FavoriteBadRequestException("존재하지 않는 즐겨찾기 id입니다", id));
    }

}
