package nextstep.subway.member.application;

import nextstep.subway.member.domain.Favorite;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.FavoriteRequest;
import nextstep.subway.member.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FavoriteService {

    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    /**
     * 즐겨찾기를 추가합니다.
     * @param id
     * @param favoriteRequest
     * @return 추가 된 즐겨찾기 ID
     */
    public Long addFavorite(Long id, FavoriteRequest favoriteRequest) {
        Member member = this.memberRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        Favorite favorite = this.toFavorite(member, favoriteRequest);
        member.addFavorite(favorite);
        this.memberRepository.flush();

        return member.getFavorites().stream()
                .filter(memberFavorite -> memberFavorite.equalsSource(favorite.getSource())
                        && memberFavorite.equalsTarget(favorite.getTarget()))
                .mapToLong(Favorite::getId)
                .findAny().orElseThrow(IllegalArgumentException::new);
    }

    /**
     * 즐겨찾기 요청의 station ID를 조회한 후 즐겨찾기로 변환합니다.
     * @param member
     * @param favoriteRequest
     * @return 즐겨찾기
     */
    private Favorite toFavorite(Member member, FavoriteRequest favoriteRequest) {
        Station source = this.stationRepository.findById(favoriteRequest.getSourceStationId())
                .orElseThrow(IllegalArgumentException::new);
        Station target = this.stationRepository.findById(favoriteRequest.getTargetStationId())
                .orElseThrow(IllegalArgumentException::new);

        return new Favorite(member, source, target);
    }

    /**
     * 해당 ID의 즐겨찾기를 모두 조회합니다.
     * @param id
     * @return 즐겨찾기 목록
     */
    public FavoriteResponse findFavorites(Long id) {
        return this.memberRepository.findById(id)
                .map(Member::getFavorites)
                .map(FavoriteResponse::new)
                .orElseThrow(IllegalArgumentException::new);
    }

    /**
     * 즐겨찾기를 삭제합니다.
     * @param id
     * @param favoriteId
     */
    public void deleteFavorite(Long id, Long favoriteId) {
        Member member = this.memberRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        Favorite favorite = member.getFavorites().stream()
                .filter(memberFavorite -> memberFavorite.equalsId(favoriteId))
                .findAny().orElseThrow(IllegalArgumentException::new);
        member.deleteFavorite(favorite);
    }
}
