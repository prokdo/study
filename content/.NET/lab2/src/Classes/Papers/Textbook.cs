namespace Classes.Papers;

public class Textbook : Book
{
    public string Subject { get; protected set; }

    public Textbook(string[] Author, string Title, string[] Content, string Subject) : base(Author, Title, Content)
    {
        this.Subject = Subject;
    }

    public override string ToString()
    {
        return $"Textbook {Title} of {Subject} by {string.Join(",", Author)}";
    }
}