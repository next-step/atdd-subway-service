package nextstep.subway.path.service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.fare.DistanceFareCalculationPolicy;
import nextstep.subway.path.domain.fare.FareCalculationPolicy;
import nextstep.subway.path.domain.fare.discount.DiscountPolicy;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nextstep.subway.path.domain.fare.discount.AgeDiscountPolicy.ADULT;

@Service
public class PathService {
    private final LineRepository lineRepository;

    public PathService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(LoginMember loginMember, Long source, Long target) {
        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = PathFinder.init(lines);
        Path shortestPath = pathFinder.findShortestPath(source, target);

        if (loginMember.isLoggedIn()) {
            int totalFare = calculateTotalFare(shortestPath, loginMember.createAgeDiscount());
            return PathResponse.of(shortestPath, totalFare);
        }

        return PathResponse.of(shortestPath, calculateTotalFare(shortestPath));
    }

    private int calculateTotalFare(Path shortestPath) {
        int distance = shortestPath.getDistance();
        FareCalculationPolicy distanceFareCalculator = new DistanceFareCalculationPolicy(ADULT, distance);
        return distanceFareCalculator.calculateFare() + shortestPath.getAdditionalFare();
    }

    private int calculateTotalFare(Path shortestPath, DiscountPolicy discountPolicy) {
        int distance = shortestPath.getDistance();
        FareCalculationPolicy distanceFareCalculator = new DistanceFareCalculationPolicy(discountPolicy, distance);
        return distanceFareCalculator.calculateFare() + shortestPath.getAdditionalFare();
    }
}
