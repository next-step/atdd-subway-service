package nextstep.graph;

public class GraphEdge<T> {
    private T source;
    private T target;
    private double weight;

    public GraphEdge(T source, T target, double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public T getSource() {
        return source;
    }

    public T getTarget() {
        return target;
    }

    public double getWeight() {
        return weight;
    }
}
