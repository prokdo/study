#include "utils/timer.hpp"

#include <stdexcept>

Timer::Timer() : _isRunning(false) {}

void Timer::start() {
    if (!_isRunning) {
        _startTime = std::chrono::high_resolution_clock::now();
        _isRunning = true;
    }
}

void Timer::stop() {
    if (_isRunning) {
        _endTime = std::chrono::high_resolution_clock::now();
        _isRunning = false;
    }
}

void Timer::update() {
    if (_isRunning) _startTime = std::chrono::high_resolution_clock::now();
}

void Timer::reset() {
    _isRunning = false;
    _startTime = std::chrono::high_resolution_clock::time_point();
    _endTime = std::chrono::high_resolution_clock::time_point();
}

double Timer::elapsedMilliseconds() const {
    if (!_isRunning && _startTime == std::chrono::high_resolution_clock::time_point())
        throw std::runtime_error("Timer has not been started yet");

    if (_isRunning) {
        auto currentTime = std::chrono::high_resolution_clock::now();
        return std::chrono::duration<double, std::milli>(currentTime - _startTime).count();
    }
    return std::chrono::duration<double, std::milli>(_endTime - _startTime).count();
}

double Timer::elapsedSeconds() const {
    if (!_isRunning && _startTime == std::chrono::high_resolution_clock::time_point())
        throw std::runtime_error("Timer has not been started yet");

    if (_isRunning) {
        auto currentTime = std::chrono::high_resolution_clock::now();
        return std::chrono::duration<double>(currentTime - _startTime).count();
    }
    return std::chrono::duration<double>(_endTime - _startTime).count();
}

double Timer::lap() const {
    if (!_isRunning) throw std::runtime_error("Timer is not running");

    auto currentTime = std::chrono::high_resolution_clock::now();
    return std::chrono::duration<double, std::milli>(currentTime - _startTime).count();
}

void Timer::restart() {
    stop();
    start();
}

bool Timer::isRunning() const {
    return _isRunning;
}