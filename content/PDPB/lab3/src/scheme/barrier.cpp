#include "scheme/barrier.hpp"
#include <stdexcept>

Barrier::Barrier(size_t count): _count(count), _current(0), _phase(0) {
    if (count == 0) throw std::invalid_argument("Count must be greater than zero.");
}

void Barrier::wait() {
    std::unique_lock<std::mutex> lock(_mutex);
    size_t current_phase = _phase;
    ++_current;
    if (_current == _count) {
        _current = 0;
        ++_phase;
        _cond.notify_all();
    }
    else _cond.wait(lock, [this, current_phase] { return current_phase != _phase; });
}

void Barrier::reset(size_t count) {
    std::lock_guard<std::mutex> lock(_mutex);
    _count = count ? count : _count;
    _current = 0;
    ++_phase;
    _cond.notify_all();
}