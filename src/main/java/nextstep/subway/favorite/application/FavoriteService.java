package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.enums.ErrorMessage;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = findMemberById(loginMember.getId());
        return member.favorites().stream().map(FavoriteResponse::from).collect(Collectors.toList());
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_MEMBER.getMessage()));
    }


    @Transactional
    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Long sourceId = favoriteRequest.findSourceId();
        Long targetId = favoriteRequest.findTargetId();

        Member member = findMemberById(loginMember.getId());
        Station sourceStation = findStationById(sourceId);
        Station targetStation = findStationById(targetId);

        Favorite favorite = Favorite.of(member, sourceStation, targetStation);
        member.addFavorite(favorite);
        return FavoriteResponse.from(favorite);
    }

    public void deleteFavorite(LoginMember loginMember, Long favoriteId) {
        Favorite favorite = findFavoriteById(favoriteId);
        Member member = findMemberById(loginMember.getId());
        member.deleteFavorite(favorite);
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(ErrorMessage.EMPTY_STATIONS.getMessage()));

    }
    private Favorite findFavoriteById(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_FAVORITE.getMessage()));
    }
}
