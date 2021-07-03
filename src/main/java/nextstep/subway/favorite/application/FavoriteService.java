package nextstep.subway.favorite.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.auth.application.ForbiddenException;
import nextstep.subway.favorite.FavoriteNotFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.MemberNotFoundException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.StationNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Favorite getFavoriteById(final Long favoriteId) {
        return favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new FavoriteNotFoundException(favoriteId));
    }

    @Transactional(readOnly = true)
    public Station getStationById(final Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException(stationId));
    }

    @Transactional(readOnly = true)
    public Member getMemberById(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(final Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);

        return favorites.stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }

    public FavoriteResponse createFavorite(final Long memberId, final FavoriteRequest request) {
        Member member = getMemberById(memberId);
        Station source = getStationById(request.getSource());
        Station target = getStationById(request.getTarget());

        Favorite favorite = Favorite.builder()
                .source(source)
                .target(target)
                .member(member)
                .build();

        return FavoriteResponse.of(favoriteRepository.save(favorite));
    }

    public void deleteFavorite(final Long memberId, final Long favoriteId) {
        Favorite favorite = getFavoriteById(favoriteId);

        if (!favorite.isOwner(memberId)) {
            throw new ForbiddenException("삭제할 권한이 없습니다.");
        }

        favoriteRepository.delete(favorite);
    }
}
