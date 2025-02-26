#pragma once

#include <chrono>

class Timer {
private:
    std::chrono::high_resolution_clock::time_point _startTime;
    std::chrono::high_resolution_clock::time_point _endTime;
    bool _isRunning;

public:
    Timer();

    void start();

    void stop();

    void update();

    void reset();

    double elapsedMilliseconds() const;

    double elapsedSeconds() const;

    double lap() const;

    void restart();

    bool isRunning() const;
};