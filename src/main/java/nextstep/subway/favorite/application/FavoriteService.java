package nextstep.subway.favorite.application;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exceptions.FavoriteNotExistException;
import nextstep.subway.exceptions.MemberNotExistException;
import nextstep.subway.exceptions.StationNotExistException;
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
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;

    public FavoriteService(final FavoriteRepository favoriteRepository, final StationRepository stationRepository,
                           final MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(final LoginMember loginMember, final FavoriteRequest favoriteRequest) {
        final Member member = findMemberById(loginMember.getId());
        final Station source = findStationById(favoriteRequest.getSource());
        final Station target = findStationById(favoriteRequest.getTarget());
        final Favorite favorite = favoriteRepository.save(Favorite.of(member, source, target));
        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findFavorites(final LoginMember loginMember) {
        final Member member = findMemberById(loginMember.getId());
        return FavoriteResponse.ofList(favoriteRepository.findByMember(member));
    }

    @Transactional
    public void deleteFavorite(final LoginMember loginMember, final Long favoriteId) {
        final Member member = findMemberById(loginMember.getId());
        checkFavoriteExist(favoriteId);
        favoriteRepository.deleteByMemberAndId(member, favoriteId);
    }

    private Member findMemberById(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotExistException::new);
    }

    private Station findStationById(final Long id) {
        return stationRepository.findById(id)
                .orElseThrow(StationNotExistException::new);
    }

    private void checkFavoriteExist(final Long favoriteId) {
        favoriteRepository.findById(favoriteId).orElseThrow(FavoriteNotExistException::new);
    }
}
