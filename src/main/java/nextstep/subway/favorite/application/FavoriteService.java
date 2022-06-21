package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberFinder;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationFinder;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {
    private MemberFinder memberFinder;
    private StationFinder stationFinder;
    private FavoriteRepository favoriteRepository;

    public FavoriteService(MemberFinder memberFinder, StationFinder stationFinder, FavoriteRepository favoriteRepository) {
        this.memberFinder = memberFinder;
        this.stationFinder = stationFinder;
        this.favoriteRepository = favoriteRepository;
    }

    public Long createFavorite(Long memberId, FavoriteRequest request) {
        Member member = memberFinder.findById(memberId);
        Station source = stationFinder.findStationById(request.getSource());
        Station target = stationFinder.findStationById(request.getTarget());

        Favorite saved = favoriteRepository.save(new Favorite(member, source, target));
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(Long memberId) {
        Member member = memberFinder.findById(memberId);
        return favoriteRepository.findAllByMember(member)
                .stream()
                .map(FavoriteResponse::new)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        Member member = memberFinder.findById(memberId);
        Favorite favorite = findById(favoriteId);
        favorite.validateOwner(member);

        favoriteRepository.deleteById(favoriteId);
    }

    @Transactional(readOnly = true)
    public Favorite findById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new NoSuchElementException("즐겨찾기를 찾을 수 없습니다."));
    }
}
