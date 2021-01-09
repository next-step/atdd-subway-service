package nextstep.subway.favorite.application;

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

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(final MemberRepository memberRepository, final StationRepository stationRepository, final FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public Long createFavorite(final LoginMember loginMember, final FavoriteRequest favoriteRequest) {
        final Member member = memberRepository.findById(loginMember.getId()).orElseThrow(NotFoundException::new);
        final Station source = stationRepository.findById(favoriteRequest.getSource()).orElseThrow(NotFoundException::new);
        final Station target = stationRepository.findById(favoriteRequest.getTarget()).orElseThrow(NotFoundException::new);

        final Favorite savedFavorite = favoriteRepository.save(Favorite.of(member, source, target));

        return savedFavorite.getId();
    }

    @Transactional
    public List<FavoriteResponse> findAllFavorites(final LoginMember loginMember) {
        final Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(NotFoundException::new);

        return member.getFavorites().stream()
            .map(FavoriteResponse::new)
            .collect(toList());
    }

    @Transactional
    public void deleteFavorite(final LoginMember loginMember, final Long id) {
        final Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(NotFoundException::new);
        favoriteRepository.findById(id).ifPresent((member::removeFavorite));
    }
}
