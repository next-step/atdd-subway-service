package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private StationRepository stationRepository;
    private MemberRepository memberRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository, MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
    }

    public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(RuntimeException::new);

        Station sourceStation = stationRepository.findById(favoriteRequest.getSource()).orElseThrow(RuntimeException::new);
        Station targetStation = stationRepository.findById(favoriteRequest.getTarget()).orElseThrow(RuntimeException::new);

        Favorite favorite = favoriteRepository.save(new Favorite(member, sourceStation, targetStation));

        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(RuntimeException::new);
        List<Favorite> favorites = favoriteRepository.findByMember(member);
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(LoginMember loginMember, Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(RuntimeException::new);
        if (!favorite.isOwner(loginMember.getId())) {
            throw new RuntimeException("본인의 즐겨찾기만 삭제할 수 있습니다.");
        }
        favoriteRepository.deleteById(id);
    }
}
