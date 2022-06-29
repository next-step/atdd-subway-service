package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.ErrorMessage;
import nextstep.subway.exception.NoSuchElementFoundException;
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

import java.util.List;

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
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(() -> new NoSuchElementFoundException(ErrorMessage.NOT_FOUND_MEMBER));
        Station sourceStation = findStationById(favoriteRequest.getSource());
        Station targetStation = findStationById(favoriteRequest.getTarget());

        Favorite persistFavorite = favoriteRepository.save(new Favorite(member, sourceStation, targetStation));

        return FavoriteResponse.from(persistFavorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberRepository.getById(loginMember.getId());
        List<Favorite> Favorites = favoriteRepository.findByMember(member);

        return FavoriteResponse.valueOf(Favorites);
    }
    @Transactional
    public void deleteFavoriteById(LoginMember loginMember, Long id) {
        Member member = memberRepository.getById(loginMember.getId());
        favoriteRepository.deleteByMemberAndId(member, id);
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(() -> new NoSuchElementFoundException(ErrorMessage.NOT_FOUND_STATION));
    }

}
