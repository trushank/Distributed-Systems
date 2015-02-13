import edu.rit.ds.registry.RegistryProxy;
import java.io.Serializable;

/**
 * Class Query encapsulates a query in the P2Pedia system. A query is uniquely
 * identified by the originating node ID and serial number. Class Query is
 * serializable so it can be passed in remote method calls. Class Query
 * implements the <TT>equals()</TT> and <TT>hashCode()</TT> methods so query
 * objects can be used as keys in hashed data structures.
 * <P>
 * Class Query also has the main program for querying the P2Pedia system.
 * <P>
 * Usage: java Query <I>host</I> <I>port</I> <I>id</I> "<I>title</I>"
 * <BR><I>host</I> = Registry Server's host
 * <BR><I>port</I> = Registry Server's port
 * <BR><I>id</I> = ID of originating node
 * <BR><I>title</I> = Article title
 */
public class Query
	implements Serializable
	{
	/**
	 * This query's originating node ID.
	 */
	public final String originNode;

	/**
	 * This query's serial number.
	 */
	public final int serialNumber;

	/**
	 * This query's article title.
	 */
	public final String title;

	/**
	 * Create a new query for the given article title. Do not call this
	 * constructor directly. Call the <TT>createQuery()</TT> method in class
	 * QueryFactory.
	 *
	 * @param  originNode    Originating node ID.
	 * @param  serialNumber  Serial number.
	 * @param  title         Article title.
	 */
	public Query
		(String originNode,
		 int serialNumber,
		 String title)
		{
		this.originNode = originNode;
		this.serialNumber = serialNumber;
		this.title = title;
		}

	/**
	 * Determine if this query equals the given object. The two are equal if
	 * they have the same originating node ID and serial number.
	 *
	 * @param  obj  Object to test.
	 *
	 * @return  True if this query equals <TT>obj</TT>, false otherwise.
	 */
	public boolean equals
		(Object obj)
		{
		return
			(obj instanceof Query) &&
			(((Query)obj).originNode.equals (this.originNode)) &&
			(((Query)obj).serialNumber == this.serialNumber);
		}

	/**
	 * Returns a hash code for this query.
	 *
	 * @return  Hash code.
	 */
	public int hashCode()
		{
		return originNode.hashCode()*31 + serialNumber;
		}

	/**
	 * Returns a string version of this query.
	 *
	 * @return  String version.
	 */
	public String toString()
		{
		return "Query ("+originNode+", "+serialNumber+", \""+title+"\")";
		}

	/**
	 * Query main program.
	 */
	public static void main
		(String[] args)
		throws Exception
		{
		// Parse command line arguments.
		if (args.length != 4) usage();
		String host = args[0];
		int port = parseInt (args[1], "port");
		String id = args[2];
		String title = args[3];

		// Look up node ID in the Registry Server and originate the query.
		RegistryProxy registry = new RegistryProxy (host, port);
		NodeRef node = (NodeRef) registry.lookup (id);
		String contents = node.query (title);

		// Print results.
		System.out.printf ("%s%n", title);
		System.out.printf ("%s%n", contents == null ? "Not found" : contents);
		}

	/**
	 * Print a usage message and exit.
	 */
	private static void usage()
		{
		System.err.println ("Usage: java Query <host> <port> <id> \"<title>\"");
		System.err.println ("<host> = Registry Server's host");
		System.err.println ("<port> = Registry Server's port");
		System.err.println ("<id> = ID of originating node");
		System.err.println ("<title> = Article title");
		System.exit (1);
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
			System.err.printf ("Query: Invalid <%s>: \"%s\"", name, arg);
			usage();
			return 0;
			}
		}
	}
