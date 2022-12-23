package nextstep.subway.favorite.application;

import java.util.List;
import javax.persistence.EntityNotFoundException;
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

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    @Transactional
    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Long sourceId = favoriteRequest.getSource();
        Long targetId = favoriteRequest.getTarget();

        Member member = memberService.findMemberById(loginMember.getId());
        Station sourceStation = stationService.findStationById(sourceId);
        Station targetStation = stationService.findStationById(targetId);

        return FavoriteResponse.from(
            favoriteRepository.save(new Favorite(member, sourceStation, targetStation))
        );
    }

    public List<FavoriteResponse> findFavoritesByMemberId(Long memberId) {

        return FavoriteResponse.favoriteResponseList(favoriteRepository.findByMemberId(memberId));
    }

    @Transactional
    public void deleteById(LoginMember loginMember, Long favoriteId) {
        Favorite favorite = favoriteRepository.findByIdAndMemberId(favoriteId, loginMember.getId()).orElseThrow(
            () -> new EntityNotFoundException("해당 즐겨찾기를 찾을 수 없습니다.")
        );
        favoriteRepository.delete(favorite);
    }

}
