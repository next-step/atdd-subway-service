package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository, MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = findMemberById(loginMember.getId());
        Station sourceStation = findStationById(favoriteRequest.getSource());
        Station targetStation = findStationById(favoriteRequest.getTarget());
        Favorite favorite = Favorite.of(member, sourceStation, targetStation);
        favoriteRepository.save(favorite);
        return FavoriteResponse.from(favorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember member) {
        return favoriteRepository.findByMemberId(member.getId())
            .stream()
            .map(FavoriteResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long id, Long memberId) {
        Favorite favorite = favoriteRepository.findByIdAndMemberId(id, memberId)
            .orElseThrow(() -> new NotFoundException());

        favoriteRepository.delete(favorite);
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());
    }
}
