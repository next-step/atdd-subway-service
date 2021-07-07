package nextstep.subway.favorite.application;

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
public class FavoriteService {

    private static final String NOT_EXISTS_FAVORITE = "존재하지 않는 즐겨찾기 입니다.";
    private static final String NOT_FAVORITE_OWNER = "로그인 한 유저의 즐겨찾기만 삭제 가능합니다.";

    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService, StationService stationService, FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = memberService.selectMember(memberId);
        Station sourceStation = stationService.findStationById(favoriteRequest.getSource());
        Station targetStation = stationService.findStationById(favoriteRequest.getTarget());
        return FavoriteResponse.of(favoriteRepository.save(new Favorite(member, sourceStation, targetStation)));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(Long memberId) {
        return FavoriteResponse.ofList(memberService.selectMember(memberId).favorites());
    }

    @Transactional
    public void deleteFavorite(Long memberId, Long favoriteId) {
        Member loginMember = memberService.selectMember(memberId);
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_FAVORITE));

        validateFavoriteOwner(loginMember, favorite);
        favoriteRepository.delete(favorite);
    }

    private void validateFavoriteOwner(Member loginMember, Favorite favorite) {
        if (!loginMember.equals(favorite.member())) {
            throw new IllegalArgumentException(NOT_FAVORITE_OWNER);
        }
    }
}
