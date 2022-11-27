package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.EntityNotFound;
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
    public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = findMemberById(loginMember.getId());
        Station sourceStation = findStationById(request.getSource());
        Station targetStation = findStationById(request.getTarget());

        return FavoriteResponse.from(
                favoriteRepository.save(new Favorite(member, sourceStation, targetStation))
        );
    }

    public List<FavoriteResponse> findFavorites(LoginMember member) {
        return favoriteRepository.findAllByMemberId(member.getId())
                .stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavoriteById(Long id) {
        favoriteRepository.deleteById(id);
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFound("회원이 존재하지 않습니다."));
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFound("지하철역이 존재하지 않습니다."));
    }
}
