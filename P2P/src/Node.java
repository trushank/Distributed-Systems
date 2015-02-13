import edu.rit.ds.registry.AlreadyBoundException;
import edu.rit.ds.registry.RegistryProxy;
import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class Node provides a Java RMI distributed node object in the P2Pedia system.
 * <P>
 * Usage: java Start Node <I>host</I> <I>port</I> <I>myid</I> <I>connid1</I>
 * <I>connid2</I> <I>file</I>
 * <BR><I>host</I> = Registry Server's host
 * <BR><I>port</I> = Registry Server's port
 * <BR><I>myid</I> = ID of this node itself
 * <BR><I>connid1</I> = ID of first node to which this node is connected
 * <BR><I>connid2</I> = ID of second node to which this node is connected
 * <BR><I>file</I> = Article file name
 */
public class Node
	implements NodeRef
	{
	private String host;
	private int port;
	private String myid;
	private String connid1;
	private String connid2;
	private String file;

	private ArticleFile articles;
	private QueryFactory factory;
	private Set<Query> pendingQueries;
	private ScheduledExecutorService reaper;
	private RegistryProxy registry;

	/**
	 * Construct a new node object.
	 * <P>
	 * The command line arguments are:
	 * <BR><TT>args[0]</TT> = Registry Server's host
	 * <BR><TT>args[1]</TT> = Registry Server's port
	 * <BR><TT>args[2]</TT> = ID of this node itself
	 * <BR><TT>args[3]</TT> = ID of first node to which this node is connected
	 * <BR><TT>args[4]</TT> = ID of second node to which this node is connected
	 * <BR><TT>args[5]</TT> = Article file name
	 *
	 * @param  args  Command line arguments.
	 *
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if there was a problem with the command
	 *     line arguments.
	 * @exception  IOException
	 *     Thrown if an I/O error or a remote error occurred.
	 */
	public Node
		(String[] args)
		throws IOException
		{
		// Parse command line arguments.
		if (args.length != 6)
			{
			throw new IllegalArgumentException
				("Usage: java Start Node <host> <port> <myid> <connid1> <connid2> <file>");
			}
		host = args[0];
		port = parseInt (args[1], "port");
		myid = args[2];
		connid1 = args[3];
		connid2 = args[4];
		file = args[5];
		if (myid.equals (connid1))
			{
			throw new IllegalArgumentException
				("Node(): <myid> = <connid1> = \""+myid+"\"");
			}
		if (myid.equals (connid2))
			{
			throw new IllegalArgumentException
				("Node(): <myid> = <connid2> = \""+myid+"\"");
			}
		if (connid1.equals (connid2))
			{
			throw new IllegalArgumentException
				("Node(): <connid1> = <connid2> = \""+connid1+"\"");
			}

		// Read article file.
		articles = new ArticleFile (file);

		// Prepare to originate queries.
		factory = new QueryFactory (myid);

		// Prepare to keep track of queries in progress.
		pendingQueries = Collections.synchronizedSet (new HashSet<Query>());
		reaper = Executors.newSingleThreadScheduledExecutor();

		// Get a proxy for the Registry Server.
		registry = new RegistryProxy (host, port);

		// Export this node.
		UnicastRemoteObject.exportObject (this, 0);

		// Bind this node into the Registry Server.
		try
			{
			registry.bind (myid, this);
			}
		catch (AlreadyBoundException exc)
			{
			try
				{
				UnicastRemoteObject.unexportObject (this, true);
				}
			catch (NoSuchObjectException exc2)
				{
				}
			throw new IllegalArgumentException
				("Node(): <myid> = \""+myid+"\" already exists");
			}
		catch (RemoteException exc)
			{
			try
				{
				UnicastRemoteObject.unexportObject (this, true);
				}
			catch (NoSuchObjectException exc2)
				{
				}
			throw exc;
			}
		}

	/**
	 * Parse an integer command line argument.
	 *
	 * @param  arg  Command line argument.
	 * @param  name  Argument name.
	 *
	 * @return  Integer value of <TT>arg</TT>.
	 *
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>arg</TT> cannot be parsed as an
	 *     integer.
	 */
	private static int parseInt
		(String arg,
		 String name)
		{
		try
			{
			return Integer.parseInt (arg);
			}
		catch (NumberFormatException exc)
			{
			throw new IllegalArgumentException
				("Node(): Invalid <"+name+">: \""+arg+"\"");
			}
		}

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
		{
		return forward (factory.createQuery (title));
		}

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
		(final Query query)
		{
		System.out.printf ("Node %s -- %s%n", myid, query);

		// Wait one second.
		slowDown();

		// If this node has the article, return the contents.
		String contents = articles.query (query.title);
		if (contents != null)
			{
			return contents;
			}

		// If this node doesn't have the article, and this node has not seen the
		// query before, forward the query.
		else if (pendingQueries.add (query))
			{
			reaper.schedule (new Runnable()
				{
				public void run()
					{
					pendingQueries.remove (query);
					}
				},
				60, TimeUnit.SECONDS);
			contents = forwardQueryTo (connid1, query);
			if (contents == null)
				contents = forwardQueryTo (connid2, query);
			return contents;
			}

		// If this node has seen the query before, do not forward the query.
		else
			{
			return null;
			}
		}

	/**
	 * Forward the given query to the node with the given ID.
	 *
	 * @param  id     Destination node ID.
	 * @param  query  Query.
	 *
	 * @return  Article contents for the given title, or null if the given title
	 *          does not exist or the query could not be forwarded.
	 *
	 * @exception  RemoteException
	 *     Thrown if a remote error occurred.
	 */
	private String forwardQueryTo
		(String id,
		 Query query)
		{
		try
			{
			NodeRef node = (NodeRef) registry.lookup (id);
			return node.forward (query);
			}
		catch (Exception exc)
			{
			return null;
			}
		}

	/**
	 * Slow down the calling thread.
	 */
	private void slowDown()
		{
		try
			{
			Thread.sleep (1000L);
			}
		catch (InterruptedException exc)
			{
			}
		}
	}
