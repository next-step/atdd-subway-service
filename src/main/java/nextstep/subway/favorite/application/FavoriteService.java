package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.NotFoundFavoritesException;
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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAllFavoritesOf(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public FavoriteResponse save(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member persistMember = memberRepository.findById(loginMember.getId()).orElseThrow(NoSuchElementException::new);
        Station sourceStation = stationRepository.findById(favoriteRequest.getSource())
                .orElseThrow(NoSuchElementException::new);
        Station targetStation = stationRepository.findById(favoriteRequest.getTarget())
                .orElseThrow(NoSuchElementException::new);
        Favorite persistFavorite = favoriteRepository.save(new Favorite(persistMember.getId(), sourceStation, targetStation));
        return FavoriteResponse.of(persistFavorite);
    }

    public void deleteFavoriteById(LoginMember loginMember, Long id) {
        favoriteRepository.deleteById(id);
    }

}
