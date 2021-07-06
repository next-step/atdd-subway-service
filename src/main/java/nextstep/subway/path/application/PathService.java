package nextstep.subway.path.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.member.application.MemberNotFoundException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.infra.RatePolicyByAddition;
import nextstep.subway.path.infra.RatePolicyByAge;
import nextstep.subway.path.infra.RatePolicyByDistance;
import nextstep.subway.station.application.StationNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Station getStationById(long id) {
        return stationRepository.findById(id).orElseThrow(() -> new StationNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Member getMemberById(long id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Lines findLines() {
        return new Lines(lineRepository.findAll());
    }

    @Transactional(readOnly = true)
    public PathResponse searchPath(final PathRequest request) {
        Path<Station> path = getPath(request);

        return PathResponse.of(path);
    }

    @Transactional(readOnly = true)
    public PathResponse searchPath(final Long memberId, final PathRequest request) {
        Path<Station> path = getPath(request);

        path.additionalFeeCalculate(new RatePolicyByAge(getMemberById(memberId).getAge()));

        return PathResponse.of(path);
    }

    private Path<Station> getPath(final PathRequest request) {
        Lines lines = findLines();
        Station source = getStationById(request.getSource());
        Station target = getStationById(request.getTarget());

        Path<Station> path = new PathFinder(lines).getPath(source, target);
        path.additionalFeeCalculate(new RatePolicyByDistance(path.getDistance()));
        path.additionalFeeCalculate(new RatePolicyByAddition(lines.getMostExpensiveFee((path.getPaths()))));

        return path;
    }
}
