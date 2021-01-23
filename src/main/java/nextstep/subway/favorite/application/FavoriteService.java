package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    final private FavoriteRepository favoritRepository;
    final private MemberRepository memberRepository;
    final private StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoritRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoritRepository = favoritRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse createFavorite(FavoriteRequest request) {
        Member member = memberRepository.findById(request.getUser().getId()).orElseThrow(IllegalArgumentException::new);
        Station station1 = stationRepository.findById(request.getSource()).orElseThrow(IllegalArgumentException::new);
        Station station2 = stationRepository.findById(request.getTarget()).orElseThrow(IllegalArgumentException::new);
        Favorite favorite = favoritRepository.save(new Favorite(member, station1, station2));
        return FavoriteResponse.of(favorite);
    }

    public List<Favorite> findFavorite() {
        return favoritRepository.findAll();
    }

    public void deleteFavorite(Long id) {
        favoritRepository.deleteById(id);
    }
}
