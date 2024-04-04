using System;
using System.IO;
using System.Security;
using lab6.Models.Proxy.Records;

namespace lab6.Models.Utils.Random;

public static class DataGenerator
{
    private static readonly System.Random Random = new();
    
    public static bool Generate(string path)
    {
        try
        {
            if (!File.Exists(path))
            {
                File.Create(path);
            }
        }
        catch (UnauthorizedAccessException)
        {
            Console.Error.WriteLine($"Denied write access to directory {Path.GetDirectoryName(path)}");
            return false;
        }
        catch (DirectoryNotFoundException)
        {
            Console.Error.WriteLine($"Cannot find directory {Path.GetDirectoryName(path)}");
            return false;
        }
        catch (Exception)
        {
            Console.Error.WriteLine($"Incorrect directory path or name: {Path.GetDirectoryName(path)}");
            return false;
        }

        var requestsNumber = Random.Next(1000, 10000);

        StreamWriter writer;
        try
        {
            writer = new StreamWriter(path);
        }
        catch (SecurityException) { 
            Console.Error.WriteLine($"Denied write access to file {Path.GetFileName(path)}");
            return false;
        }

        for (var _ = 0; _ < requestsNumber; _++)
        {
            var requester = new Client("255.255.255.255", 25565);
            var requested = new Client("255.255.255.255", 25566);

            var request = new Request(
                DateOnly.FromDateTime(DateTime.Now), 
                TimeOnly.FromDateTime(DateTime.Now), 
                requester,
                requested,
                123456789);
            
            writer.WriteLine(request);
        }
        
        writer.Close();
        
        return true;
    }
}