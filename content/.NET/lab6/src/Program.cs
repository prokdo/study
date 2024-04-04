using Avalonia;
using System;
using lab6.Models.Proxy.Files;
using lab6.Models.Utils.Random;

namespace lab6;

public static class Program
{
    // Initialization code. Don't use any Avalonia, third-party APIs or any
    // SynchronizationContext-reliant code before AppMain is called: things aren't initialized
    // yet and stuff might break.
    // [STAThread]
    public static void Main(string[] args)
    {
        DataGenerator.Generate("/home/prokdo/Университет/Git/study/content/.NET/lab6/rsrc/log.txt");
        
        var log = LogReader.Read("/home/prokdo/Университет/Git/study/content/.NET/lab6/rsrc/log.txt");
        
        foreach (var request in log!)
        {
            Console.WriteLine(request);
        }
        
        // BuildAvaloniaApp().StartWithClassicDesktopLifetime(args);
    }

    // Avalonia configuration, don't remove; also used by visual designer.
    // private static AppBuilder BuildAvaloniaApp()
    //     => AppBuilder.Configure<App>()
    //         .UsePlatformDetect()
    //         .WithInterFont()
    //         .LogToTrace();
}