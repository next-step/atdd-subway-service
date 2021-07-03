package nextstep.subway.path.domain.policy;

import nextstep.subway.path.domain.policy.fare.discount.ChildDiscountByAgeStrategy;
import nextstep.subway.path.domain.policy.fare.discount.TeenagerDiscountByAgeStrategy;
import nextstep.subway.path.domain.policy.fare.distance.GraterThan10KmNotMoreThan50KmOverFareByDistanceStrategy;
import nextstep.subway.path.domain.policy.fare.distance.GraterThan50KmOverFareByDistanceStrategy;
import nextstep.subway.path.domain.policy.fare.distance.NotMoreThan10KmOverFareByDistanceStrategy;
import nextstep.subway.path.domain.policy.fare.line.DefaultOverFareByLineStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PolicyConfig {
    @Bean
    public ChildDiscountByAgeStrategy childDiscountByAgeStrategy() {
        return new ChildDiscountByAgeStrategy();
    }

    @Bean
    public TeenagerDiscountByAgeStrategy teenagerDiscountByAgeStrategy() {
        return new TeenagerDiscountByAgeStrategy();
    }

    @Bean
    public NotMoreThan10KmOverFareByDistanceStrategy notMoreThan10KmOverFareByDistanceStrategy() {
        return new NotMoreThan10KmOverFareByDistanceStrategy();
    }

    @Bean
    public GraterThan10KmNotMoreThan50KmOverFareByDistanceStrategy graterThan10KmNotMoreThan50KmOverFareByDistanceStrategy() {
        return new GraterThan10KmNotMoreThan50KmOverFareByDistanceStrategy();
    }

    @Bean
    public GraterThan50KmOverFareByDistanceStrategy graterThan50KmOverFareByDistanceStrategy() {
        return new GraterThan50KmOverFareByDistanceStrategy();
    }

    @Bean
    public DefaultOverFareByLineStrategy defaultOverFareByLineStrategy() {
        return new DefaultOverFareByLineStrategy();
    }
}
