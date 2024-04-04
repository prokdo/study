using System.Text;

namespace lab6.Models.Proxy.Records;

public record Client(
    string IP,
    ushort Port)
{
    public override string ToString()
    {
        var builder = new StringBuilder();

        builder.Append(IP);
        builder.Append('\t');
        builder.Append(Port);
        
        return builder.ToString();
    }
}