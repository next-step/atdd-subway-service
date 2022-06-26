package nextstep.subway.navigation.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.application.FareCalculatorService;
import nextstep.subway.fare.domain.DiscountPolicy;
import nextstep.subway.fare.domain.DiscountType;
import nextstep.subway.line.domain.Line;
import nextstep.subway.navigation.domain.Navigation;
import nextstep.subway.navigation.dto.NavigationResponse;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NavigationService {

    public NavigationResponse findShortest(
            List<Line> persistLines,
            Station sourceStation,
            Station targetStation,
            LoginMember loginMember
    ) {
        Navigation navigation = Navigation.of(persistLines);
        NavigationResponse shortest = navigation.findShortest(sourceStation, targetStation);
        DiscountPolicy discountPolicy = DiscountType.ofDiscountPolicy(loginMember.getAge());

        int fare = FareCalculatorService.calculate(shortest.getDistance()) + navigation.maxLineExtraFare();
        int discountedFare = discountPolicy.discountedFare(fare);

        return NavigationResponse.ofNavigationAndFare(shortest.getStations(), shortest.getDistance(), discountedFare);
    }
}
