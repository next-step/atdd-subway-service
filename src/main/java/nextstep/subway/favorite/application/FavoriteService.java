package nextstep.subway.favorite.application;

import nextstep.subway.common.ErrorCode;
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

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           StationRepository stationRepository,
                           MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Favorite create(Long memberId, FavoriteRequest request) {
        Station sourceStation = findStationById(request.getSource());
        Station targetStation = findStationById(request.getTarget());
        Member member = findMemberById(memberId);

        return favoriteRepository.save(Favorite.from(member, sourceStation, targetStation));
    }

    private Station findStationById(Long request) {
        return stationRepository.findById(request)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NO_EXISTS_STATION_IN_FAVORITE.getErrorMessage()));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NO_EXISTS_MEMBER_IN_FAVORITE.getErrorMessage()));
    }

    public List<FavoriteResponse> findAllFavorites(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(memberId);
        return favorites.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long memberId, Long favoriteId) {
        Favorite favorite = favoriteRepository.findByIdAndMemberId(favoriteId, memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NO_EXISTS_FAVORITE.getErrorMessage()));
        favoriteRepository.delete(favorite);
    }
}
