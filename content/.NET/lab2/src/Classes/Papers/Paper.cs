namespace Classes.Papers;

public class Paper<SomeType>
{
    public SomeType? Content { get; set; }
    public int FoldCount { get; protected set; }

    public Paper(SomeType? Content)
    {
        this.Content = Content;
        this.FoldCount = 0;
    }

    virtual public void Fold()
    {
        if (FoldCount < 7)
        {
            FoldCount++;
        }
        else
        {
            throw new Exception("Paper is already folded 7 times");
        }
    }

    virtual public void Unfold()
    {
        if (FoldCount > 0)
        {
            FoldCount--;
        }
        else
        {
            throw new Exception("Paper is already unfolded");
        }
    }

    public override string ToString()
    {
        return $"Paper with {Content} content";
    }
}