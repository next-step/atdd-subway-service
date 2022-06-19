package nextstep.subway.favorite.application;

import nextstep.subway.auth.application.ForbiddenException;
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
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final MemberService memberService;
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(MemberService memberService, FavoriteRepository favoriteRepository, StationService stationService) {
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    @Transactional
    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest request) {
        Member member = memberService.findMemberById(memberId);
        Station source = stationService.findStationById(request.getSource());
        Station target = stationService.findStationById(request.getTarget());
        Favorite favorite = favoriteRepository.save(new Favorite(member, source, target));

        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMember_Id(memberId);

        return favorites.stream()
                .map(favorite -> FavoriteResponse.of(favorite))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long memberId, Long favoriteId) {
        Member member = memberService.findMemberById(memberId);
        Favorite favorite = findFavoriteById(favoriteId);
        if (!favorite.isOwnedBy(member)) {
            throw new ForbiddenException("권한이 없습니다.");
        }

        favoriteRepository.delete(favorite);
    }

    private Favorite findFavoriteById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new NoSuchElementException("입력한 ID를 가진 즐겨찾기가 없습니다."));
    }
}
