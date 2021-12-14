package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    private MemberRepository memberRepository;

    private StationRepository stationRepository;

    private FavoriteRepository favoriteRepository;

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(RuntimeException::new);
        Station sourceStation = stationRepository.findById(request.getSourceStationId()).orElseThrow(RuntimeException::new);
        Station targetStation = stationRepository.findById(request.getTargetStationId()).orElseThrow(RuntimeException::new);
        Favorite favorite = new Favorite(member, sourceStation, targetStation);
        Favorite persistFavorite = favoriteRepository.save(favorite);
        return FavoriteResponse.of(persistFavorite);
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        List<Favorite> favorites = member.getFavorites();
        return FavoriteResponse.from(favorites);
    }

    public void deleteFavorite(Long id, Long id1) {
    }

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository, FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }
}
