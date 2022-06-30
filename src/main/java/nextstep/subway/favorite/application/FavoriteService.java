package nextstep.subway.favorite.application;

import static nextstep.subway.exception.domain.SubwayExceptionMessage.NOT_MINE_FAVORITE;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.domain.SubwayException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    private final MemberRepository memberRepository;


    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService,
                           MemberRepository memberRepository) {
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favorite) {
        final Member member = getMember(loginMember);
        final Station sourceStation = stationService.findStationById(favorite.getSourceId());
        final Station targetStation = stationService.findStationById(favorite.getTargetId());
        final Favorite persistFavorite = favoriteRepository.save(new Favorite(member, sourceStation, targetStation));
        return FavoriteResponse.of(persistFavorite);
    }

    private Member getMember(LoginMember loginMember) {
        return memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
    }

    public List<FavoriteResponse> findAllFavorites(LoginMember loginMember) {
        final Member member = getMember(loginMember);
        return favoriteRepository.findAllByMember(member)
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(LoginMember loginMember, long id) {
        final Member member = getMember(loginMember);
        final Favorite favorite = getFavorite(id);
        validateMyFavorite(member, favorite);
        favoriteRepository.delete(favorite);
    }

    private void validateMyFavorite(Member member, Favorite favorite) {
        if (!favorite.isMyFavorite(member)) {
            throw new SubwayException(NOT_MINE_FAVORITE);
        }
    }

    private Favorite getFavorite(long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("즐겨찾기를 찾을수 없습니다."));
    }
}
