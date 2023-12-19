using Utils;

namespace Classes.Papers;

public class Painting : Paper<Image>
{
    public string[] Author { get; protected set; }
    public string Genre { get; protected set; }
    public string Title { get; protected set; }

    public Painting(Image Content, string[] Author, string Genre, string Title) : base(Content)
    {
        this.Author = Author;
        this.Genre = Genre;
        this.Title = Title;
    }

    public override void Fold()
    {
        if (FoldCount == 0)
        {
            FoldCount++;
        }
        else
        {
            throw new Exception("It is already rolled up");
        }
    }

    public override void Unfold()
    {
        if (FoldCount == 1)
        {
            FoldCount--;
        }
        else
        {
            throw new Exception("It is already unrolled");
        }
    }

    public override string ToString()
    {
        return $"Painting by {string.Join(",", Author)} in genre {Genre} with {Content} content";
    }
}