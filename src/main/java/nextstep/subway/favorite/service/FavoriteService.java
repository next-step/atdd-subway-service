package nextstep.subway.favorite.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Transactional(readOnly = true)
@Service
public class FavoriteService {
    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService,
        StationService stationService, FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse createFavorite(Long loginMemberId, FavoriteRequest request) {
        Member member = memberService.findById(loginMemberId);
        Station sourceStation = stationService.findStationById(request.getSource());
        Station targetStation = stationService.findStationById(request.getTarget());
        Favorite favorite = Favorite.of(member, sourceStation, targetStation);
        favoriteRepository.save(favorite);
        return FavoriteResponse.from(favorite);
    }

    public FavoriteResponse findFavoriteById(Long loginMemberId, Long id) {
        Member member = memberService.findById(loginMemberId);
        Favorite favorite = findByIdAndMember(id, member);
        return FavoriteResponse.from(favorite);
    }

    public List<FavoriteResponse> findFavorites(Long loginMemberId) {
        Member member = memberService.findById(loginMemberId);
        List<Favorite> favorites = findAllMemberFavorites(member);
        return favorites.stream()
            .map(FavoriteResponse::from)
            .collect(Collectors.toList());
    }

    public void deleteFavoriteById(Long loginMemberId, Long id) {
        Member member = memberService.findById(loginMemberId);
        Favorite favorite = findByIdAndMember(id, member);

        favoriteRepository.delete(favorite);
    }

    private Favorite findByIdAndMember(Long id, Member member) {
        return favoriteRepository.findByIdAndMember(id, member)
            .orElseThrow(() -> new IllegalArgumentException("유저의 해당 즐겨찾기를 찾을 수 없습니다. id: " + id));
    }

    private List<Favorite> findAllMemberFavorites(Member member) {
        return favoriteRepository.findAllByMember(member);
    }
}
