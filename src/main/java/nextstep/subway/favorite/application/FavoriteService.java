package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.FavoriteNotFoundException;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private MemberService memberService;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Station sourceStation = stationService.findById(favoriteRequest.getSource());
        Station targetStation = stationService.findById(favoriteRequest.getTarget());
        Member member = memberService.findMemberById(memberId);
        return FavoriteResponse.of(favoriteRepository.save(new Favorite(member, sourceStation, targetStation)));
    }

    public List<FavoriteResponse> findAllFavoriteByMemberId(Long memberId) {
        return favoriteRepository.findByMemberId(memberId)
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void removeFavorite(Long id) {
        Favorite favorite = findFavoriteById(id);
        favoriteRepository.delete(favorite);
    }

    public Favorite findFavoriteById(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(FavoriteNotFoundException::new);
    }
}
