namespace Classes.Papers;

public class Magazine : Paper<string[]>
{
    public string Publisher { get; protected set; }
    public int PageAmount { get; protected set; }
    public int CurrentPage { get; protected set; }
    public string Title { get; protected set; }

    public Magazine(string Publisher, string Title, string[] Content) : base(Content)
    {
        this.Publisher = Publisher;
        this.Title = Title;
        this.PageAmount = Content.Length;
    }

    override public void Fold()
    {
        if (FoldCount < 3)
        {
            FoldCount++;
        }
        else
        {
            throw new Exception("Magazine is already folded 3 times");
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
            throw new Exception("Magazine is already unfolded");
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

    public override string ToString()
    {
        return $"Magazine {Title} by {Publisher}";
    }
}