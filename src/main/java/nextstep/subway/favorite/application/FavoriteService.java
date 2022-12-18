package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationRepository stationRepository;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse createFavorite(FavoriteRequest favoriteRequest, LoginMember loginMember) {
        Optional<Station> sourceStation = stationRepository.findById(Long.valueOf(favoriteRequest.getSource()));
        Optional<Station> targetStation = stationRepository.findById(Long.valueOf(favoriteRequest.getTarget()));
        Optional<Member> member = memberRepository.findById(loginMember.getId());

        validCheckForExistStation(sourceStation, targetStation);

        Favorite savedFavorite = favoriteRepository.save(new Favorite(sourceStation.get(), targetStation.get(), member.get()));
        return FavoriteResponse.of(savedFavorite);
    }

    private void validCheckForExistStation(Optional<Station> sourceStation, Optional<Station> targetStation) {
        if (!sourceStation.isPresent() || !targetStation.isPresent()) throw new RuntimeException();
    }
}
