namespace lab2;

using Classes.*;
using Utils;

public class Program
{
    readonly static Image image = new();
    readonly static string[] pages = new string[] { "Page 1", "Page 2", "Page 3" };
    readonly static string[] authors = new string[] { "Author1", "Author2", "Author3" };

    public static void Main(string[] args)
    {
        Paper<Image> paper1 = new(image);
        Paper<string[]> paper2 = new(pages);
        Paper<string> paper3 = new("Sample text");

        Book book1 = new(authors, "Book1", pages);
        Book book2 = new(new ArraySegment<string>(authors, 0, 2).ToArray<string>(), "Book2", new ArraySegment<string>(pages, 0, 2).ToArray<string>());
        Book book3 = new(new ArraySegment<string>(authors, 2, 1).ToArray<string>(), "Book3", pages);

        Magazine magazine1 = new("Publisher1", "Magazine1", pages);
        Magazine magazine2 = new("Publisher2", "Magazine2", new ArraySegment<string>(pages, 1, 2).ToArray<string>());

        Newspaper newspaper1 = new("Publisher1", pages);

        Poster poster1 = new(image, authors, "Poster1");

        Textbook textbook1 = new(authors, "Textbook1", pages, "Math");
        Textbook textbook2 = new(authors, "Textbook2", pages, "Physics");

        Painting painting1 = new(image, authors, "Painting", "Painting1");

        Library library = new();
        library.Add(book1);
        library.Add(book2);
        library.Add(book3);
        library.Add(magazine1);
        library.Add(magazine2);
        library.Add(newspaper1);
        library.Add(book1);

        Console.WriteLine(library);

        Console.WriteLine(library.Give(book1));
        Console.WriteLine(library.Give(book1));
        try { Console.WriteLine(library.Give(book1)); } catch (Exception e) { Console.WriteLine(e.Message); }
        try { library.Add(painting1); } catch (Exception e) { Console.WriteLine(e.Message); }

        Console.WriteLine(library);

        Newsstand newsstand = new();
        newsstand.Add(magazine1, 10);
        newsstand.Add(magazine2, 20);
        newsstand.Add(newspaper1, 30);

        Console.WriteLine(newsstand);

        Console.WriteLine(newsstand.Sell(magazine1, 10));
        try { Console.WriteLine(newsstand.Sell(magazine1, 10)); } catch (Exception e) { Console.WriteLine(e.Message); }

        try { Console.WriteLine(newsstand.Sell(magazine2, 10)); } catch (Exception e) { Console.WriteLine(e.Message); }

        while (true)
        {
            try
            {
                paper1.Fold();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                break;
            }
        }
        paper1.Unfold();

        while (true)
        {
            try
            {
                book1.Fold();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                break;
            }
        }

        while (true)
        {
            try
            {
                poster1.Fold();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                break;
            }
        }
        poster1.Unfold();
    }
}