package nextstep.subway.line.domain;

public interface OperationCostPolicy {
    Charge basicCharge();
    Charge policyCharge(long item);
}
