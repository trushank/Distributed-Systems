/**
 * Class QueryFactory provides an object for creating query objects in the
 * P2Pedia system.
 */
public class QueryFactory
	{
	/**
	 * Originating node ID for newly constructed queries.
	 */
	private String originNode;

	/**
	 * Serial number for newly constructed queries.
	 */
	private int serialNumber;

	/**
	 * Create a new query factory. The given originating node ID will be filled
	 * into every newly created query.
	 *
	 * @param  originNode  Originating node ID.
	 */
	public QueryFactory
		(String originNode)
		{
		this.originNode = originNode;
		}

	/**
	 * Create a new query for the given article title.
	 *
	 * @param  title  Article title.
	 */
	public Query createQuery
		(String title)
		{
		return new Query (originNode, ++ serialNumber, title);
		}
	}
