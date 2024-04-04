using System;
using System.Text;

namespace lab6.Models.Proxy.Records;

public record Request(
    DateOnly Date,
    TimeOnly Time,
    Client Requester,
    Client Requested,
    long DataSize)
{
    public override string ToString()
    {
        var builder = new StringBuilder();

        builder.Append(Date);
        builder.Append('\t');
        builder.Append(Time);
        builder.Append('\t');
        builder.Append(Requester);
        builder.Append('\t');
        builder.Append(Requested);
        builder.Append('\t');
        builder.Append(DataSize);
        
        return builder.ToString();
    }
}