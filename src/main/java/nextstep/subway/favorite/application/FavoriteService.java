package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.constant.ErrorCode;
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
        List<Favorite> favorites = favoriteRepository.findByMemberId(loginMember.getId());
        return favorites.stream().map(FavoriteResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Long sourceId = favoriteRequest.getSourceId();
        Long targetId = favoriteRequest.getTargetId();

        Member member = findMemberById(loginMember.getId());
        Station sourceStation = findStationById(sourceId);
        Station targetStation = findStationById(targetId);

        Favorite favorite = favoriteRepository.save(Favorite.of(member, sourceStation, targetStation));
        return FavoriteResponse.from(favorite);
    }


    public void removeFavorite(LoginMember loginMember, Long favoriteId) {
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.존재하지_않는_회원.getErrorMessage()));
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(ErrorCode.존재하지_않는_역.getErrorMessage()));
    }
}
