package nextstep.subway.favorite.application;

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

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        final Station source = stationService.findById(request.getSource());
        final Station target = stationService.findById(request.getTarget());
        final Member member = memberService.findById(loginMember.getId());
        Favorite favorite = favoriteRepository.save(Favorite.of(member, source, target));
        return FavoriteResponse.of(favorite);
    }

    public void deleteFavorite(LoginMember loginMember, final Long id) {
        final Member member = memberService.findById(loginMember.getId());
        if (!favoriteRepository.existsByIdAndMember(id, member)) {
            throw new IllegalArgumentException("해당 유저의 즐겨찾기가 존재하지 않습니다.");
        }
        favoriteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAll(LoginMember loginMember) {
        return favoriteRepository.findAllByMemberId(loginMember.getId())
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }
}
