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

    @Transactional
    public FavoriteResponse saveFavorite(final Long loginId, final FavoriteRequest request) {
        final Member member = memberService.findMemberById(loginId);
        final Station source = stationService.findStationById(request.getSource());
        final Station target = stationService.findStationById(request.getTarget());
        final Favorite persistFavorite = favoriteRepository.save(Favorite.of(member, source, target));
        return FavoriteResponse.from(persistFavorite);
    }

    public FavoriteResponse findLineResponseById(final Long loginId, final Long id) {
        final Member member = memberService.findMemberById(loginId);
        final Favorite favorite = favoriteRepository.findByIdAndMember(id, member);
        return FavoriteResponse.from(favorite);
    }

    @Transactional
    public void deleteLineById(final Long loginId, final Long id) {
        final Member member = memberService.findMemberById(loginId);
        favoriteRepository.deleteByIdAndMember(id, member);
    }
}
