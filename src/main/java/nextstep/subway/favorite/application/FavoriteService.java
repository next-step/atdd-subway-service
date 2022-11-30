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
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;

    public FavoriteService(
            FavoriteRepository favoriteRepository,
            StationRepository stationRepository,
            MemberRepository memberRepository
    ) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public FavoriteResponse saveFavorite(final LoginMember loginMember, final FavoriteRequest request) {
        Member member = findMemberById(loginMember.getId());
        Station sourceStation = findStationById(request.getSourceStationId());
        Station targetStation = findStationById(request.getTargetStationId());

        return FavoriteResponse.from(
                favoriteRepository.save(new Favorite(member, sourceStation, targetStation))
        );
    }

    private Member findMemberById(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 회원을 찾을 수 없습니다."));
    }

    private Station findStationById(final Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 Station을 찾을 수 없습니다."));
    }

    public List<FavoriteResponse> findFavorites(LoginMember member) {
        return Collections.emptyList();
    }

    @Transactional
    public void deleteFavoriteById(Long id, Long memberId) {
    }
}
