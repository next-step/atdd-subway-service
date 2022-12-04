package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.ErrorMessage;
import nextstep.subway.exception.EntityNotFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(MemberRepository memberRepository, StationService stationService, FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }
    @Transactional
    public FavoriteResponse create(Long memberId, FavoriteRequest favoriteRequest) {
        Station source = stationService.findStationById(favoriteRequest.getSource());
        Station target = stationService.findStationById(favoriteRequest.getTarget());
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException("Member", memberId)
        );
        Favorite actual = favoriteRepository.save(Favorite.of(member, source, target));
        return FavoriteResponse.of(actual);
    }

    public List<FavoriteResponse> findAll(Long memberId) {
        return favoriteRepository.findAllByMemberId(memberId)
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }
    @Transactional
    public void remove(Long memberId, Long favoriteId) {
        Favorite favorite = favoriteRepository.findByIdAndMemberId(memberId, favoriteId).orElseThrow(
                ()-> new IllegalStateException(ErrorMessage.cannotFindFavorite(memberId, favoriteId))
        );
        favoriteRepository.delete(favorite);
    }

}
