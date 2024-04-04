using System;
using System.Collections.Generic;
using System.IO;
using lab6.Models.Proxy.Records;

namespace lab6.Models.Proxy.Files;

public static class LogReader
{
    public static List<Request>? Read(string path)
    {
        if (!File.Exists(path))
        {
            Console.Error.WriteLine(
                $"Cannot find file {Path.GetFileName(path)} in directory {Path.GetDirectoryName(path)} or directory itself");
            return null;
        }

        var result = new List<Request>();

        using var reader = new StreamReader(path);
        var position = 0;
        while (reader.ReadLine() is { } line)
        {
            try
            {
                var splitted = line.Split('\t');

                var requester = new Client(splitted[2], ushort.Parse(splitted[3]));
                var requested = new Client(splitted[4], ushort.Parse(splitted[5]));

                result.Add(new Request(
                    DateOnly.Parse(splitted[0]),
                    TimeOnly.Parse(splitted[1]),
                    requester,
                    requested,
                    long.Parse(splitted[6])
                ));
            }
            catch (Exception exception)
            {
                Console.Error.WriteLine($"An exception occurred at log line {position}.\n" +
                                        $"Exception stack trace: {exception.StackTrace}");
                return null;
            }

            position++;
        }

        return result;
    }
}