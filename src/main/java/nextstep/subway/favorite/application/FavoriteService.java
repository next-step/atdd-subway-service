package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.ExceptionType;
import nextstep.subway.exception.NotFoundException;
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
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    @Transactional
    public FavoriteResponse registerFavorite(LoginMember loginMember, FavoriteRequest request) {
        Station source = stationService.findById(request.getSourceId());
        Station target = stationService.findById(request.getTargetId());

        Member member = memberService.findById(loginMember.getId());
        Favorite favorite = Favorite.of(source, target, member);
        favoriteRepository.save(favorite);
        return FavoriteResponse.of(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAll(LoginMember loginMember) {
        Member member = memberService.findById(loginMember.getId());
        List<Favorite> favorites = favoriteRepository.findAllByMember(member);

        return favorites.stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public void removeFavorite(LoginMember loginMember, Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.NOT_FOUND_FAVORITE.getMessage(favoriteId)));

        Member member = memberService.findById(loginMember.getId());
        favorite.validateMember(member);

        favoriteRepository.delete(favorite);
    }
}
