package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private StationRepository stationRepository;
    private MemberRepository memberRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository, MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
    }

    public FavoriteResponse saveFavorite(Long id, FavoriteRequest favoriteRequest) {
        Favorite favorite = favoriteRepository.save(new Favorite(getStation(favoriteRequest.getSource()), getStation(favoriteRequest.getTarget()), getMember(id)));
        return new FavoriteResponse(favorite.getId(), StationResponse.of(favorite.getSource()), StationResponse.of(favorite.getTarget()));
    }

    private Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("사용자 조회 실패")
                );
    }

    private Station getStation(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("역 조회 실패")
                );
    }
}
