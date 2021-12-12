package nextstep.subway.favorite.application;

import nextstep.subway.favorite.application.exception.FavoritePathNotFoundException;
import nextstep.subway.favorite.domain.FavoritePath;
import nextstep.subway.favorite.domain.FavoritePathRepository;
import nextstep.subway.favorite.dto.FavoritePathRequest;
import nextstep.subway.favorite.dto.FavoritePathResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoritePathService {

    private final MemberService memberService;
    private final StationService stationService;
    private final FavoritePathRepository favoritePathRepository;

    public FavoritePathService(MemberService memberService, StationService stationService, FavoritePathRepository favoritePathRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoritePathRepository = favoritePathRepository;
    }

    @Transactional
    public FavoritePathResponse save(Long memberId, FavoritePathRequest request) {
        Member member = memberService.findById(memberId);
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());

        FavoritePath favoritePath = favoritePathRepository.save(request.toFavoritePath(member, source, target));
        return FavoritePathResponse.of(favoritePath);
    }

    public List<FavoritePathResponse> findAll(Long id) {
        List<FavoritePath> favoritePaths = favoritePathRepository.findByMemberId(id);
        return favoritePaths.stream().map(FavoritePathResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long favoriteId) {
        FavoritePath favoritePath = favoritePathRepository.findById(favoriteId)
                .orElseThrow(FavoritePathNotFoundException::new);
        favoritePathRepository.delete(favoritePath);
    }
}
