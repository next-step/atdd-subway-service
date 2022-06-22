package nextstep.subway.favorite.application;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exceptions.FavoriteNotExistException;
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
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(final FavoriteRepository favoriteRepository, final MemberService memberService,
                           final StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    @Transactional
    public FavoriteResponse createFavorite(final LoginMember loginMember, final FavoriteRequest favoriteRequest) {
        final Member member = memberService.findMemberById(loginMember.getId());
        final Station source = stationService.findStationById(favoriteRequest.getSource());
        final Station target = stationService.findStationById(favoriteRequest.getTarget());
        final Favorite favorite = favoriteRepository.save(Favorite.of(member, source, target));
        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findFavorites(final LoginMember loginMember) {
        final Member member = memberService.findMemberById(loginMember.getId());
        return FavoriteResponse.ofList(favoriteRepository.findByMember(member));
    }

    @Transactional
    public void deleteFavorite(final LoginMember loginMember, final Long favoriteId) {
        final Member member = memberService.findMemberById(loginMember.getId());
        checkFavoriteExist(favoriteId);
        favoriteRepository.deleteByMemberAndId(member, favoriteId);
    }

    private void checkFavoriteExist(final Long favoriteId) {
        favoriteRepository.findById(favoriteId).orElseThrow(FavoriteNotExistException::new);
    }
}
