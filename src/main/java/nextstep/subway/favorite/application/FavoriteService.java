package nextstep.subway.favorite.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

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

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Station sourceStation = stationRepository.findById(favoriteRequest.getSource()).orElseThrow(NoSuchElementException::new);
        Station targetStation = stationRepository.findById(favoriteRequest.getTarget()).orElseThrow(NoSuchElementException::new);
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(NoSuchElementException::new);

        Favorite favorite = Favorite.of(member, sourceStation, targetStation);
        Favorite savedFavorite = favoriteRepository.save(favorite);

        return FavoriteResponse.of(savedFavorite.getId(), savedFavorite.getSource(), savedFavorite.getTarget());
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());
        return favorites.stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }
}
