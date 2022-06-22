package nextstep.subway.favorite.infrastructure;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.infrastructure.InMemoryMemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.infrastructure.InMemoryStationRepository;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryFavoriteRepository implements FavoriteRepository {
    private final Map<Long, Favorite> elements = new HashMap<>();
    private long favoriteId = 0L;

    public InMemoryFavoriteRepository() {
        InMemoryMemberRepository memberRepository = new InMemoryMemberRepository();
        Member member = memberRepository.findById(1L).orElse(null);

        InMemoryStationRepository stationRepository = new InMemoryStationRepository();
        Station 강남역 = stationRepository.findById(1L).orElse(null);
        Station 양재역 = stationRepository.findById(2L).orElse(null);
        Station 남부터미널역 = stationRepository.findById(3L).orElse(null);
        Station 교대역 = stationRepository.findById(4L).orElse(null);

        save(new Favorite(member, 강남역, 교대역));
        save(new Favorite(member, 양재역, 남부터미널역));
    }

    @Override
    public Favorite save(Favorite favorite) {
        ReflectionTestUtils.setField(favorite, "id", ++favoriteId);
        elements.put(favoriteId, favorite);
        return favorite;
    }

    @Override
    public List<Favorite> findAllByMember(Member member) {
        return elements.values()
                .stream()
                .filter(favorite -> favorite.isOwner(member))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Favorite> findById(Long favoriteId) {
        return Optional.ofNullable(elements.get(favoriteId));
    }

    @Override
    public void deleteById(Long favoriteId) {
        elements.remove(favoriteId);
    }
}
