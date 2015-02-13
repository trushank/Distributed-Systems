import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class ArticleFile encapsulates a file of encyclopedia articles in the P2Pedia
 * system.
 */
public class ArticleFile
	{
	/**
	 * Map from article title to article contents.
	 */
	private HashMap<String,String> articles;

	/**
	 * Construct a new article file.
	 *
	 * @param  filename  Article file name.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public ArticleFile
		(String filename)
		throws IOException
		{
		articles = new HashMap<String,String>();
		Scanner scanner = null;
		try
			{
			scanner = new Scanner (new File (filename));
			while (scanner.hasNextLine())
				{
				String title = scanner.nextLine();
				if (articles.containsKey (title))
					{
					throw new IOException
						("ArticleFile(): File \""+filename+
						 "\": Duplicate title: \""+title+"\"");
					}
				if (! scanner.hasNextLine())
					{
					throw new IOException
						("ArticleFile(): File \""+filename+
						 "\": Missing contents for title \""+title+"\"");
					}
				String contents = scanner.nextLine();
				articles.put (title, contents);
				}
			}
		finally
			{
			if (scanner != null) scanner.close();
			}
		}

	/**
	 * Query this article file for the given title.
	 *
	 * @param  title  Title.
	 *
	 * @return  Contents for the given title, or null if the given title does
	 *          not exist.
	 */
	public String query
		(String title)
		{
		return articles.get (title);
		}
	}
