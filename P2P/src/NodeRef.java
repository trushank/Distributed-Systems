import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface NodeRef specifies the Java RMI remote interface for a distributed
 * node object in the P2Pedia system.
 */
public interface NodeRef
	extends Remote
	{
	/**
	 * Query this node for the given article title. This method is called by the
	 * query client program to originate a query at this node.
	 *
	 * @param  title  Article title.
	 *
	 * @return  Article contents for the given title, or null if the given title
	 *          does not exist.
	 *
	 * @exception  RemoteException
	 *     Thrown if a remote error occurred.
	 */
	public String query
		(String title)
		throws RemoteException;

	/**
	 * Forward the given query to this node. This method is called by another
	 * node that is forwarding the query to this node.
	 *
	 * @param  query  Query.
	 *
	 * @return  Article contents for the given title, or null if the given title
	 *          does not exist.
	 *
	 * @exception  RemoteException
	 *     Thrown if a remote error occurred.
	 */
	public String forward
		(Query query)
		throws RemoteException;
	}
