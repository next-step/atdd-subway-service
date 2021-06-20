package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class FavoriteService {

    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(MemberRepository memberRepository, FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);
        Station source = stationRepository.findById(favoriteRequest.getSource()).orElseThrow(NoSuchElementException::new);
        Station target = stationRepository.findById(favoriteRequest.getTarget()).orElseThrow(NoSuchElementException::new);

        Favorite persistFavorite = favoriteRepository.save(new Favorite(member, source, target));

        return FavoriteResponse.of(persistFavorite);
    }
}
