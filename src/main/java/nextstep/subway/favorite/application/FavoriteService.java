package nextstep.subway.favorite.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.auth.application.AuthorizationException;
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        validateCreateFavorite(favoriteRequest);
        Favorite persistFavorite = favoriteRepository.save(createFavoriteEntity(loginMember, favoriteRequest));
        return persistFavorite.getId();
    }

    private void validateCreateFavorite(FavoriteRequest favoriteRequest) {
        if (Objects.equals(favoriteRequest.getSource(), favoriteRequest.getTarget())) {
            throw new IllegalArgumentException("즐겨찾기의 출발역과 도착역은 같을 수 없습니다.");
        }
    }

    private Favorite createFavoriteEntity(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = getMember(loginMember);

        List<Station> findResult = findAllByIdIn(favoriteRequest);

        Station source = getStation(findResult, favoriteRequest.getSource());
        Station target = getStation(findResult, favoriteRequest.getTarget());

        return Favorite.builder()
                .member(member)
                .source(source)
                .target(target)
                .build();
    }

    private Member getMember(LoginMember loginMember) {
        return memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new AuthorizationException("만료된 ID 입니다."));
    }

    private List<Station> findAllByIdIn(FavoriteRequest favoriteRequest) {
        return stationRepository.findAllByIdIn(
                Arrays.asList(favoriteRequest.getSource(), favoriteRequest.getTarget()));
    }

    private Station getStation(List<Station> findResult, Long stationId) {
        return findResult.stream()
                .filter(station -> station.isSameStation(stationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 출발역입니다."));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> getFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(LoginMember loginMember, Long favoriteId) {
        Favorite favorite = favoriteRepository.findByIdAndMemberId(favoriteId, loginMember.getId())
                .orElseThrow(() -> new FavoriteValidationException("즐겨찾기가 존재하지 않습니다."));
        favoriteRepository.delete(favorite);
    }
}
