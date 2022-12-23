package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteCreatedRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    public static final String FAVORITE_DUPLICATE_EXCEPTION_MESSAGE = "출발역과 도착역이 같은 즐겨찾기를 생성할 수 없다.";

    private MemberRepository memberRepository;
    private StationRepository stationRepository;
    private FavoriteRepository favoriteRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository, FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse create(Long loginMemberId, FavoriteCreatedRequest request) {
        Member member = memberRepository.findById(loginMemberId).orElseThrow(EntityNotFoundException::new);
        Station source = stationRepository.findById(request.getSourceId()).orElseThrow(EntityNotFoundException::new);
        Station target = stationRepository.findById(request.getTargetId()).orElseThrow(EntityNotFoundException::new);
        if (favoriteRepository.existsBySourceIdAndTargetId(source.getId(), target.getId())) {
            throw new IllegalArgumentException(FAVORITE_DUPLICATE_EXCEPTION_MESSAGE);
        }
        return new FavoriteResponse(favoriteRepository.save(new Favorite(member, source, target)));
    }

    public List<FavoriteResponse> findAll(Long loginMemberId) {
        return favoriteRepository.findAllByMemberId(loginMemberId).stream()
                .map(FavoriteResponse::new)
                .collect(Collectors.toList());
    }

    public void delete(Long loginMemberId, Long favoriteId) {
        favoriteRepository.findByIdAndMemberId(favoriteId, loginMemberId).orElseThrow(EntityNotFoundException::new);
        favoriteRepository.deleteById(favoriteId);
    }
}
