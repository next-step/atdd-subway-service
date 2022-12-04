package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.FavoriteExceptionCode;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.exception.MemberExceptionCode;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.StationExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class FavoriteService {

    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(MemberRepository memberRepository, FavoriteRepository favoriteRepository,
            StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest request) {
        Member member = findMemberId(memberId);
        Station source = findStationById(request.getSource());
        Station target = findStationById(request.getTarget());
        Favorite favorite = favoriteRepository.save(new Favorite(member, source, target));

        return FavoriteResponse.of(favorite);
    }

    private Member findMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(MemberExceptionCode.NOT_FOUND_BY_ID.getMessage()));
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new NotFoundException(StationExceptionCode.NOT_FOUND_BY_ID.getMessage()));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAllFavorites(Long memberId) {
        return favoriteRepository.findAllByMemberId(memberId).stream()
                .map(FavoriteResponse::of)
                .collect(toList());
    }

    @Transactional
    public void deleteFavorite(AuthMember loginMember, Long favoriteId) {
        Favorite favorite = findById(favoriteId);
        favorite.checkLoginMember(loginMember.getId());
        favoriteRepository.delete(favorite);
    }

    private Favorite findById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new NotFoundException(FavoriteExceptionCode.NOT_FOUND_BY_ID.getMessage()));
    }
}
