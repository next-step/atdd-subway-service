package nextstep.subway.favorite.application;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.exception.MemberNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.StationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository,
        StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse saveFavorite(LoginMember loginMember,
        FavoriteRequest favoriteRequest) {
        Member member = findByMemberId(loginMember.getId());
        Station source = findByStationId(favoriteRequest.getSource());
        Station target = findByStationId(favoriteRequest.getTarget());
        Favorite persistFavorite = favoriteRepository.save(
            favoriteRequest.toFavorite(member, source, target));
        favoriteRepository.save(persistFavorite);
        return FavoriteResponse.of(persistFavorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());
        return FavoriteResponse.ofList(favorites);
    }

    public void deleteFavoriteById(LoginMember loginMember, Long id) {
        Favorite favorite = favoriteRepository.findByMemberIdAndId(loginMember.getId(), id)
            .orElseThrow(FavoriteNotFoundException::new);

        favoriteRepository.delete(favorite);
    }

    @Transactional(readOnly = true)
    public Member findByMemberId(final Long id) {
        return memberRepository.findById(id)
            .orElseThrow(MemberNotFoundException::new);
    }

    public Station findByStationId(final Long id) {
        return stationRepository.findById(id)
            .orElseThrow(StationNotFoundException::new);
    }
}
