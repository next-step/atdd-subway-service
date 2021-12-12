package nextstep.subway.favorite.application;

import nextstep.subway.favorite.application.exception.FavoriteNotFoundException;
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
@Transactional(readOnly = true)
public class FavoriteService {

    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService, StationService stationService, FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse save(Long memberId, FavoriteRequest request) {
        Member member = memberService.findById(memberId);
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());

        Favorite favorite = favoriteRepository.save(request.toFavorite(member, source, target));
        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findAll(Long id) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(id);
        return FavoriteResponse.ofList(favorites);
    }

    @Transactional
    public void delete(Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(FavoriteNotFoundException::new);
        favoriteRepository.delete(favorite);
    }
}
