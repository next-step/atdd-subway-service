package nextstep.subway.favorite.application;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.common.exception.NoResultException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponses;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public Long createFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = findMemberById(memberId);
        Station sourceStation = findStationById(favoriteRequest.getSourceId());
        Station targetStation = findStationById(favoriteRequest.getTargetId());
        Favorite favorite = Favorite.of(member, sourceStation, targetStation);
        Favorite saveFavorite = favoriteRepository.save(favorite);

        return saveFavorite.getId();
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new NoResultException("존재하지 않는 출발역입니다."));
    }

    @Transactional(readOnly = true)
    public FavoriteResponses findFavorites(Long memberId) {
        Member member = findMemberById(memberId);
        List<Favorite> favorites = favoriteRepository.findByMember(member);

        return new FavoriteResponses(favorites);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoResultException("존재하지 않는 사용자입니다."));
    }

    @Transactional
    public void deleteFavorite(Long memberId, Long id) {
        validate(memberId, id);
        favoriteRepository.deleteById(id);
    }

    private void validate(Long memberId, Long id) {
        if (!favoriteRepository.existsByMember_IdAndId(memberId, id)) {
            throw new AuthorizationException("본인의 즐겨찾기만 삭제할 수 있습니다.");
        }
    }

}
