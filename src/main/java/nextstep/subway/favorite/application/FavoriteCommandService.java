package nextstep.subway.favorite.application;

import nextstep.subway.auth.application.ApproveException;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.LineHasNotExistStationException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
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
@Transactional
public class FavoriteCommandService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;


    public FavoriteCommandService(StationRepository stationRepository, LineRepository lineRepository, MemberRepository memberRepository, FavoriteRepository favoriteRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.memberRepository = memberRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Station source = findStationById(request.getSource());
        Station target = findStationById(request.getTarget());
        Member member = findMemberByEmail(loginMember.getEmail());

        validateLineContainsStations(source, target);

        return FavoriteResponse.of(favoriteRepository.save(new Favorite(member, source, target)));
    }

    public void deleteById(LoginMember loginMember, Long id) {
        Member member = findMemberByEmail(loginMember.getEmail());
        Favorite favorite = findById(id);

        validateOwner(member, favorite);

        favoriteRepository.delete(favorite);
    }

    private void validateOwner(Member member, Favorite favorite) {
        if (!favorite.isOwner(member)) {
            throw new ApproveException();
        }
    }

    private void validateLineContainsStations(Station source, Station target) {
        Lines lines = new Lines(findAllLines());

        if (!lines.containsStationsExactly(source, target)) {
            throw new LineHasNotExistStationException();
        }
    }

    private Favorite findById(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(AuthorizationException::new);
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    private List<Line> findAllLines() {
        return lineRepository.findAll();
    }
}
