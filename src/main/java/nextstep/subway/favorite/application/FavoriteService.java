package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.exception.FavoriteException;
import nextstep.subway.line.exception.MemberException;
import nextstep.subway.line.exception.StationException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.line.application.LineService.STATION_NOT_FOUND_MESSAGE;

@Service
@Transactional
public class FavoriteService {
    public static final String MEMBER_NOT_FOUND_MESSAGE = "회원이 없습니다.";
    public static final String FAVORITE_NOT_FOUND_MESSAGE = "즐겨찾기가 없습니다.";
    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = findMemberById(loginMember);
        List<Station> stations = findAllStationsByIds(favoriteRequest);
        Station source = getStation(favoriteRequest.getSource(), stations);
        Station target = getStation(favoriteRequest.getTarget(), stations);

        Favorite persistFavorite = favoriteRepository.save(new Favorite(member, source, target));
        return FavoriteResponse.from(persistFavorite);
    }

    public Station getStation(Long stationId, List<Station> stations) {
        return stations.stream().filter(station -> station.getId() == stationId).findFirst().orElseThrow(() -> new StationException(STATION_NOT_FOUND_MESSAGE));
    }

    @Transactional(readOnly = true)
    public List<Station> findAllStationsByIds(FavoriteRequest favoriteRequest) {
        List<Long> list = Arrays.asList(favoriteRequest.getSource(), favoriteRequest.getTarget());
        return stationRepository.findAllById(list);
    }

    @Transactional(readOnly = true)
    public Member findMemberById(LoginMember loginMember) {
        return memberRepository.findById(loginMember.getId()).orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND_MESSAGE));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findMyFavorites(LoginMember loginMember) {
        List<Favorite> persistFavorites = favoriteRepository.findAllByMemberId(loginMember.getId());
        return FavoriteResponse.ofList(persistFavorites);
    }

    public void deleteFavorites(LoginMember loginMember, Long favoritesId) {
        Favorite favorite = findByIdAndMemberId(favoritesId, loginMember);
        favoriteRepository.deleteById(favorite.getId());
    }

    @Transactional(readOnly = true)
    public Favorite findByIdAndMemberId(Long id, LoginMember loginMember) {
        return favoriteRepository.findByIdAndMemberId(id, loginMember.getId()).orElseThrow(() -> new FavoriteException(FAVORITE_NOT_FOUND_MESSAGE));
    }
}
