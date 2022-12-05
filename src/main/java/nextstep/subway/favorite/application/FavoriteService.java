package nextstep.subway.favorite.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {
    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(
        MemberService memberService,
        StationService stationService,
        FavoriteRepository favoriteRepository
    ) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse createFavorite(Long loginMemberId, FavoriteRequest favoriteRequest) {
        Member member = memberService.findById(loginMemberId);
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());
        validateAlreadyExist(member, source, target);
        Favorite favorite = new Favorite(source, target, member);
        favoriteRepository.save(favorite);
        return FavoriteResponse.from(favorite);
    }

    private void validateAlreadyExist(Member member, Station source, Station target) {
        Optional<Favorite> favorite = favoriteRepository.findBySourceAndTargetAndMember(source, target, member);
        if(favorite.isPresent()){
            throw new RuntimeException("이미 등록하였습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> getFavorites(Long loginMemberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMemberId);
        return favorites.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long loginMemberId, Long id) {
        Favorite favorite = favoriteRepository.findByIdAndMemberId(id, loginMemberId).orElseThrow(() -> new RuntimeException("즐겨찾기를 찾을 수 없습니다."));
        favoriteRepository.delete(favorite);
    }
}
