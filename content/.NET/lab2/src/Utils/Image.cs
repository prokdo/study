namespace Utils;

public class Image
{
    protected static string DRAWING_SYMBOLS = " .,:;i1tfLCG08@";
    protected static char DEFAULT_DRAWING_SYMBOL = '#';


    public int Width { get; protected set; }
    public int Height { get; protected set; }
    public char[][] Content { get; set; }

    public Image(int Width, int Height)
    {
        this.Width = Width;
        this.Height = Height;
        this.Content = new char[Height][];
        for (int i = 0; i < Height; i++)
        {
            this.Content[i] = new char[Width];
        }

        for (int i = 0; i < Height; i++)
        {
            for (int j = 0; j < Width; j++)
            {
                this.Content[i][j] = DEFAULT_DRAWING_SYMBOL;
            }
        }
    }

    public Image(int Width, int Height, char[][] Content)
    {
        this.Width = Width;
        this.Height = Height;
        this.Content = Content;
    }

    public Image(char[][] Content)
    {
        this.Width = Content[0].Length;
        this.Height = Content.Length;
        this.Content = Content;
    }

    public Image()
    {
        Random random = new();
        this.Width = random.Next(1, 100);
        this.Height = random.Next(1, 100);
        this.Content = new char[Height][];
        for (int i = 0; i < Height; i++)
        {
            this.Content[i] = new char[Width];
        }

        for (int i = 0; i < Height; i++)
        {
            for (int j = 0; j < Width; j++)
            {
                this.Content[i][j] = DRAWING_SYMBOLS[random.Next(0, DRAWING_SYMBOLS.Length)];
            }
        }
    }

    public void Draw(int X, int Y, char Symbol)
    {
        if (DRAWING_SYMBOLS.Contains(Symbol))
        {
            this.Content[Y][X] = Symbol;
        }
        else
        {
            throw new Exception("Symbol is not a valid drawing symbol");
        }
    }

    public void Print()
    {
        for (int i = 0; i < Height; i++)
        {
            Console.WriteLine(this.Content[i]);
        }
    }
}