package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(final FavoriteRepository favoriteRepository, final MemberService memberService, final StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(final LoginMember loginMember, final FavoriteRequest request) {
        final Member member = getMemberByLoginMember(loginMember);
        final Station sourceStation = stationService.findStationById(request.getSource());
        final Station targetStation = stationService.findStationById(request.getTarget());
        final Favorite favorite = favoriteRepository.save(new Favorite(member, sourceStation, targetStation));
        return getFavoriteResponseByFavorite(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAllFavoriteResponses(final LoginMember loginMember) {
        final Member member = getMemberByLoginMember(loginMember);
        return favoriteRepository.findAllByMember(member)
                .stream()
                .map(this::getFavoriteResponseByFavorite)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(final Long id) {
        favoriteRepository.deleteById(id);
    }

    private Member getMemberByLoginMember(final LoginMember loginMember) {
        return memberService.findMemberById(loginMember.getId());
    }

    private FavoriteResponse getFavoriteResponseByFavorite(final Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                MemberResponse.of(favorite.getMember()),
                StationResponse.of(favorite.getSourceStation()),
                StationResponse.of(favorite.getTargetStation()));
    }
}
