#include "utils/timer.hpp"

#include <stdexcept>

Timer::Timer() : __isRunning(false) {}

Timer::~Timer() {
    if (__isRunning) {
        stop();
    }
}

void Timer::start() {
    if (!__isRunning) {
        __startTime = std::chrono::high_resolution_clock::now();
        __isRunning = true;
    }
}

void Timer::stop() {
    if (__isRunning) {
        __endTime = std::chrono::high_resolution_clock::now();
        __isRunning = false;
    }
}

void Timer::update() {
    if (__isRunning) {
        __startTime = std::chrono::high_resolution_clock::now();
    }
}

void Timer::reset() {
    __isRunning = false;
    __startTime = std::chrono::high_resolution_clock::time_point();
    __endTime = std::chrono::high_resolution_clock::time_point();
}

double Timer::elapsedMilliseconds() const {
    if (!__isRunning && __startTime == std::chrono::high_resolution_clock::time_point()) {
        throw std::runtime_error("Timer has not been started yet");
    }

    if (__isRunning) {
        auto currentTime = std::chrono::high_resolution_clock::now();
        return std::chrono::duration<double, std::milli>(currentTime - __startTime).count();
    }
    return std::chrono::duration<double, std::milli>(__endTime - __startTime).count();
}

double Timer::elapsedSeconds() const {
    if (!__isRunning && __startTime == std::chrono::high_resolution_clock::time_point()) {
        throw std::runtime_error("Timer has not been started yet");
    }

    if (__isRunning) {
        auto currentTime = std::chrono::high_resolution_clock::now();
        return std::chrono::duration<double>(currentTime - __startTime).count();
    }
    return std::chrono::duration<double>(__endTime - __startTime).count();
}

double Timer::lap() const {
    if (!__isRunning) {
        throw std::runtime_error("Timer is not running");
    }
    auto currentTime = std::chrono::high_resolution_clock::now();
    return std::chrono::duration<double, std::milli>(currentTime - __startTime).count();
}

bool Timer::isRunning() const {
    return __isRunning;
}