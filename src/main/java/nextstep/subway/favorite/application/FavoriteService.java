package nextstep.subway.favorite.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(
        MemberRepository memberRepository,
        StationRepository stationRepository,
        FavoriteRepository favoriteRepository
    ) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse createFavorite(Long loginMemberId, FavoriteRequest favoriteRequest) {
        Member member = memberRepository.findById(loginMemberId).orElseThrow(RuntimeException::new);
        Station source = stationRepository.findById(favoriteRequest.getSource()).orElseThrow(RuntimeException::new);
        Station target = stationRepository.findById(favoriteRequest.getTarget()).orElseThrow(RuntimeException::new);
        validateAlreadyExist(member, source, target);
        Favorite favorite = favoriteRepository.save(new Favorite(source, target, member));
        return FavoriteResponse.from(favorite);
    }

    private void validateAlreadyExist(Member member, Station source, Station target) {
        Optional<Favorite> favorite = favoriteRepository.findBySourceAndTargetAndMember(source, target, member);
        if(favorite.isPresent()){
            throw new RuntimeException("이미 등록하였습니다.");
        }
    }

    public List<FavoriteResponse> getFavorites(Long loginMemberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMember_Id(loginMemberId);
        return favorites.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
