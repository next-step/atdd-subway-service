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
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {

    private MemberRepository memberRepository;
    private StationRepository stationRepository;
    private FavoriteRepository favoriteRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository, FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        // memberId를 이용하여 회원 정보 조회
        Member member = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);

        // source, target 정보를 이용하여 지하철역 조회
        Station sourceStation = stationRepository.findById(favoriteRequest.getSource()).orElseThrow(RuntimeException::new);
        Station targetStation = stationRepository.findById(favoriteRequest.getTarget()).orElseThrow(RuntimeException::new);
        Favorite favorite = new Favorite(member, sourceStation, targetStation);
        // 즐겨 찾기 저장
        return new FavoriteResponse(1L, StationResponse.of(sourceStation), StationResponse.of(targetStation));
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(loginMember.getId());
        LocalDateTime now = LocalDateTime.now();
        return Arrays.asList(new FavoriteResponse(loginMember.getId(), new StationResponse(1L, "서초역", now, now), new StationResponse(3L, "역삼역", now, now)));
    }

    public void deleteFavorite(LoginMember loginMember, Long id) {
        Optional<Member> findMember = memberRepository.findById(loginMember.getId());
    }
}
