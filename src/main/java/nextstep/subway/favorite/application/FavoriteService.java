package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;

    public FavoriteService(MemberService memberService, StationService stationService, FavoriteRepository favoriteRepository, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
    }

    public FavoriteResponse saveFavorite(FavoriteRequest favoriteRequest) {
        Member member = memberService.selectMember(favoriteRequest.getMemberId());
        Station sourceStation = stationService.findStationById(favoriteRequest.getSource());
        Station targetStation = stationService.findStationById(favoriteRequest.getTarget());

        Favorite favorite = new Favorite(member, sourceStation, targetStation);
        member.addFavorite(favorite);
        favoriteRepository.save(favorite);
        memberRepository.save(member);

        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findFavorite(Long memberId) {
        Member member = memberService.selectMember(memberId);
        return FavoriteResponse.ofList(member.favorites());
    }

    public void deleteFavorite(Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 즐겨찾기 입니다."));

        favorite.deleteFavorite();
        favoriteRepository.delete(favorite);
    }
}
