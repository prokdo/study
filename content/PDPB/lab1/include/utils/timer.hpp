#pragma once

#include <chrono>

class Timer {
public:
    Timer();

    ~Timer();

    void start();

    void stop();

    void update();

    double elapsedMilliseconds() const;

    double elapsedSeconds() const;

    bool isRunning() const;

private:
    std::chrono::high_resolution_clock::time_point __startTime;
    std::chrono::high_resolution_clock::time_point __endTime;
    bool __isRunning;
};