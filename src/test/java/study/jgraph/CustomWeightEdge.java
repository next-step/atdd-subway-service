package study.jgraph;

import org.jgrapht.graph.DefaultWeightedEdge;

public class CustomWeightEdge extends	DefaultWeightedEdge {

	private String label;

	/**
	 * Constructs a relationship edge
	 *
	 * @param label the label of the new edge.
	 *
	 */
	public CustomWeightEdge(String label)
	{
		this.label = label;
	}

	/**
	 * Gets the label associated with this edge.
	 *
	 * @return edge label
	 */
	public String getLabel()
	{
		return label;
	}

	@Override
	public String toString()
	{
		return "(" + getSource() + " : " + getTarget() + " : " + label + ")";
	}
}
