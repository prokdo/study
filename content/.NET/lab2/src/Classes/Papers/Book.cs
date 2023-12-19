namespace Classes.Papers;

public class Book : Paper<string[]>
{
    public string[] Author { get; protected set; }
    public string Title { get; protected set; }
    public int PageAmount { get; protected set; }
    public int CurrentPage { get; protected set; }

    public Book(string[] Author, string Title, string[] Content) : base(Content)
    {
        this.Author = Author;
        this.Title = Title;
        this.PageAmount = Content.Length;
    }

    public string OpenAtPage(int Page)
    {
        if (Page > PageAmount)
        {
            throw new Exception("Page number is greater than page amount");
        }
        else
        {
            CurrentPage = Page;
            return Content[Page];
        }
    }

    public string OpenAtPage()
    {
        return Content[CurrentPage];
    }

    public string NextPage()
    {
        if (CurrentPage < PageAmount)
        {
            CurrentPage++;
            return Content[CurrentPage];
        }
        else
        {
            throw new Exception("Current page is already the last page");
        }
    }

    public string PreviousPage()
    {
        if (CurrentPage > 0)
        {
            CurrentPage--;
            return Content[CurrentPage];
        }
        else
        {
            throw new Exception("Current page is already the first page");
        }
    }

    public void Close()
    {
        CurrentPage = 0;
    }

    public override void Fold()
    {
        throw new Exception("Book cannot be folded");
    }

    public override void Unfold()
    {
        throw new Exception("Book cannot be unfolded");
    }

    public override string ToString()
    {
        return $"Book {Title} by {string.Join(",", Author)}";
    }
}