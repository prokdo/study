#pragma once

#include <mutex>
#include <optional>
#include <shared_mutex>

template <typename T>
class ReaderWriter {
private:
    T _data;
    mutable std::shared_mutex _mutex;

public:
    explicit ReaderWriter(const T& initial_value = T()) : _data(initial_value) {}

    void write(const T& value) {
        std::unique_lock lock(_mutex);
        _data = value;
    }

    T read() const {
        std::shared_lock lock(_mutex);
        return _data;
    }

    template <typename Func>
    void update(Func&& func) {
        std::unique_lock lock(_mutex);
        func(_data);
    }

    std::optional<T> try_read() const {
        if (_mutex.try_lock_shared()) {
            std::shared_lock lock(_mutex, std::adopt_lock);
            return _data;
        }
        return std::nullopt;
    }

    bool try_write(const T& value) {
        if (_mutex.try_lock()) {
            std::unique_lock lock(_mutex, std::adopt_lock);
            _data = value;
            return true;
        }
        return false;
    }
};