package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.FavoriteException;
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
@Transactional
public class FavoriteService {

    private static final String NOT_FOUND_MEMBER_ERROR_MESSAGE = "회원 정보를 찾을 수 없습니다.";
    private static final String NOT_FOUND_STATION_ERROR_MESSAGE = "출발지 또는 도착지 지하철역을 찾을 수 없습니다.";

    private MemberRepository memberRepository;
    private StationRepository stationRepository;
    private FavoriteRepository favoriteRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository, FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = findMember(loginMember);
        Station sourceStation = findStation(favoriteRequest.getSource());
        Station targetStation = findStation(favoriteRequest.getTarget());
        Favorite favorite = Favorite.create(member.getId(), sourceStation, targetStation);
        Favorite saveFavorite = favoriteRepository.save(favorite);
        return FavoriteResponse.of(saveFavorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(loginMember.getId());
        return favorites.stream().map(FavoriteResponse::of).collect(Collectors.toList());
    }

    public void deleteFavorite(LoginMember loginMember, Long id) {
        findMember(loginMember);
        favoriteRepository.deleteById(id);
    }

    private Member findMember(LoginMember loginMember) {
        return memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new FavoriteException(NOT_FOUND_MEMBER_ERROR_MESSAGE));
    }

    private Station findStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new FavoriteException(NOT_FOUND_STATION_ERROR_MESSAGE));
    }
}
