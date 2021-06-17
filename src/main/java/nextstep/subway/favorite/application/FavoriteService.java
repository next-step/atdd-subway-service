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

@Service
public class FavoriteService {

  private final MemberRepository memberRepository;
  private final StationRepository stationRepository;
  private final FavoriteRepository favoriteRepository;

  public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository, FavoriteRepository favoriteRepository) {
    this.memberRepository = memberRepository;
    this.stationRepository = stationRepository;
    this.favoriteRepository = favoriteRepository;
  }

  public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest request) {
    Member member = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
    Station sourceStation = stationRepository.findById(request.getSourceStationId()).orElseThrow(RuntimeException::new);
    Station targetStation = stationRepository.findById(request.getTargetStationId()).orElseThrow(RuntimeException::new);
    return FavoriteResponse.of(favoriteRepository.save(new Favorite(member, sourceStation, targetStation)));
  }


}
