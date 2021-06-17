package nextstep.subway.favorite.application;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.exception.StationNotExistException;
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
import java.util.stream.Collectors;

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
    Member member = findMember(memberId);
    Station sourceStation = findStation(request.getSourceStationId());
    Station targetStation = findStation(request.getTargetStationId());
    return FavoriteResponse.of(favoriteRepository.save(new Favorite(member, sourceStation, targetStation)));
  }

  public List<FavoriteResponse> findFavorites(Long memberId) {
    Member member = findMember(memberId);
    return favoriteRepository.findAllByMember(member)
                          .stream()
                          .map(FavoriteResponse::of)
                          .collect(Collectors.toList());
  }

  private Member findMember(Long memberId) {
    return memberRepository.findById(memberId)
              .orElseThrow(AuthorizationException::new);
  }

  private Station findStation(Long stationId) {
    return stationRepository.findById(stationId)
            .orElseThrow(StationNotExistException::new);
  }
}
