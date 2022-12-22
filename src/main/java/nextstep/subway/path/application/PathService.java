package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.DistanceFare;
import nextstep.subway.fare.FarePolicy;
import nextstep.subway.fare.discount.AgeDiscount;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static nextstep.subway.fare.discount.AgeDiscount.create;

@Service
public class PathService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findPath(LoginMember member, Long sourceId, Long targetId) {

        List<Line> findLines = lineRepository.findAll();

        Station source = stationRepository.findById(sourceId).orElseThrow(EntityNotFoundException::new);
        Station target = stationRepository.findById(targetId).orElseThrow(EntityNotFoundException::new);

        PathFinder pathFinder = new PathFinder(source, target, findLines);
        int distanceFare = new DistanceFare(pathFinder.findDistance()).calculate();
        AgeDiscount ageDiscount = create(member.getAge());
        int fare = FarePolicy.of(distanceFare, pathFinder.findLineFare(), ageDiscount.getDeductionFare(), ageDiscount.getDiscountRate()).calculate();
        return new PathResponse(pathFinder, fare);
    }
}
