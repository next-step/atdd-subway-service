package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional (readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;


    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public FavoriteResponse add(LoginMember member, FavoriteRequest request) {
        Station sourceStation = getStation(request.getSource());
        Station targetStation = getStation(request.getTarget());

        Favorite favorite = Favorite.of(new Member(member.getId(), member.getEmail(), member.getAge()), sourceStation, targetStation);
        Favorite saveFavorite = favoriteRepository.save(favorite);
        return new FavoriteResponse(saveFavorite.getId(), saveFavorite.getSource(), saveFavorite.getTarget());
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 지하철이 등록되어있지 않습니다."));
    }

    public List<Favorite> getFavorites(LoginMember loginMember) {
        return favoriteRepository.findAllByMember(new Member(loginMember.getId(), loginMember.getEmail(), loginMember.getAge()));
    }
    @Transactional
    public void deleteFavorite(Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                                                .orElseThrow(() -> new IllegalArgumentException("선택한 즐겨찾기가 없습니다."));
        favoriteRepository.delete(favorite);
    }

}
