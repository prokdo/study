namespace Classes.Papers;

public class Newspaper : Paper<string[]>
{
    public string Publisher { get; protected set; }
    public int PageAmount { get; protected set; }
    public int CurrentPage { get; protected set; }

    public Newspaper(string Publisher, string[] Content) : base(Content)
    {
        this.Publisher = Publisher;
        this.PageAmount = Content.Length;
    }

    override public void Fold()
    {
        if (FoldCount < 6)
        {
            FoldCount++;
        }
        else
        {
            throw new Exception("Newspaper is already folded 6 times");
        }
    }

    override public void Unfold()
    {
        if (FoldCount > 0)
        {
            FoldCount--;
        }
        else
        {
            throw new Exception("Newspaper is already unfolded");
        }
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

    public override string ToString()
    {
        return $"Newspaper published by {Publisher}";
    }
}