using Utils;

namespace Classes.Papers;

public class Poster : Painting
{
    public Poster(Image Content, string[] Author, string Title) : base(Content, Author, "Poster", Title)
    {
    }

    public override string ToString()
    {
        return $"Poster {Title} by {string.Join(",", Author)} with {Content} content";
    }
}