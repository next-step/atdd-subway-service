package nextstep.subway.favorite.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
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
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           StationRepository stationRepository,
                           MemberRepository memberRepository) {

        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
    }

    public FavoriteResponse saveFavorite(long memberId, FavoriteRequest favoriteRequest) {
        Member member = memberRepository.getById(memberId);
        Station source = stationRepository.getById(favoriteRequest.getSource());
        Station target = stationRepository.getById(favoriteRequest.getTarget());

        final Favorite save = favoriteRepository.save(new Favorite(member, source, target));

        return FavoriteResponse.of(save);
    }

    @Transactional(readOnly = true)
    public FavoriteResponse searchFavorite(long favoriteId) {
        return favoriteRepository.findById(favoriteId)
                .map(FavoriteResponse::of)
                .orElseThrow(() -> new NoSuchElementException("해당 즐겨찾기를 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> searchMemberFavorite(long memberId) {
        return favoriteRepository.findAllByMemberId(memberId)
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(long memberId, long favoriteId) {
        final Favorite favorite = favoriteRepository.findByMemberIdAndId(memberId, favoriteId)
                .orElseThrow(() -> new NoSuchElementException("해당 즐겨찾기를 찾을 수 없습니다."));

        favoriteRepository.delete(favorite);
    }
}
