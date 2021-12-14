package nextstep.subway.favorite.application;

import nextstep.subway.ServiceException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
  private FavoriteRepository favoriteRepository;
  private MemberRepository memberRepository;
  private StationRepository stationRepository;

  public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
    this.favoriteRepository = favoriteRepository;
    this.memberRepository = memberRepository;
    this.stationRepository = stationRepository;
  }

  public FavoriteResponse createFavorite(Long loginMemberId, FavoriteRequest request) {
    Member member = findMember(loginMemberId);
    Station sourceStation = findStation(request.getSource());
    Station targetStation = findStation(request.getTarget());

    checkCreationDuplicate(loginMemberId, sourceStation, targetStation);

    Favorite favorite = favoriteRepository.save(Favorite.of(member, sourceStation, targetStation));
    return FavoriteResponse.of(favorite);
  }

  public List<FavoriteResponse> findAll(Long loginMemberId) {
    return favoriteRepository.findAllByMemberId(loginMemberId)
            .stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
  }

  public void deleteFavorite(Long favoriteId, Long memberId) {
    Favorite favorite = favoriteRepository.findByIdAndMemberId(favoriteId, memberId)
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST, "해당 즐겨찾기 항목은 삭제할 수 없습니다."));
    favoriteRepository.delete(favorite);
  }

  private Station findStation(Long stationId) {
    return stationRepository.findById(stationId)
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST, "등록되지 않은 지하철역을 즐겨찾기 추가할 수 없습니다."));
  }

  private Member findMember(Long loginMemberId) {
    return memberRepository.findById(loginMemberId)
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST, "잘못된 접근 요청입니다."));
  }

  private void checkCreationDuplicate(Long loginMemberId, Station sourceStation, Station targetStation) {
    favoriteRepository.findByMemberId(loginMemberId)
            .forEach(favorite -> favorite.checkDuplicate(sourceStation, targetStation));
  }
}
