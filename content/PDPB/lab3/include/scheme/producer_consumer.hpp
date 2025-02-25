#pragma once

#include <atomic>
#include <condition_variable>
#include <mutex>
#include <queue>
#include <stdexcept>
#include <thread>

template <typename T>
class ProducerConsumerConditional {
private:
    std::queue<T> _buffer;
    size_t _max_size;
    mutable std::mutex _mutex;
    std::condition_variable _cond_producer;
    std::condition_variable _cond_consumer;

public:
    explicit ProducerConsumerConditional(size_t max_size = 3)
        : _max_size(max_size) {
        if (max_size == 0) {
            throw std::invalid_argument("max_size must be greater than zero.");
        }
    }

    void produce(T value) {
        std::unique_lock<std::mutex> lock(_mutex);
        _cond_producer.wait(lock, [this]() { return _buffer.size() < _max_size; });
        _buffer.push(std::move(value));
        _cond_consumer.notify_all();
    }

    T consume() {
        std::unique_lock<std::mutex> lock(_mutex);
        _cond_consumer.wait(lock, [this]() { return !_buffer.empty(); });
        T value = std::move(_buffer.front());
        _buffer.pop();
        _cond_producer.notify_all();
        return value;
    }

    bool isEmpty() const {
        std::lock_guard<std::mutex> lock(_mutex);
        return _buffer.empty();
    }

    bool isFull() const {
        std::lock_guard<std::mutex> lock(_mutex);
        return _buffer.size() >= _max_size;
    }

    size_t size() const {
        std::lock_guard<std::mutex> lock(_mutex);
        return _buffer.size();
    }
};

template <typename T>
class ProducerConsumerAtomic {
private:
    std::queue<T> _buffer;
    size_t _max_size;
    mutable std::mutex _mutex;
    std::atomic<bool> _can_produce{true};
    std::atomic<bool> _can_consume{false};

public:
    explicit ProducerConsumerAtomic(size_t max_size = 3) : _max_size(max_size) {}

    void produce(T value) {
        std::unique_lock<std::mutex> lock(_mutex);
        while (_buffer.size() >= _max_size) {
            lock.unlock();
            std::this_thread::sleep_for(std::chrono::milliseconds(1));
            lock.lock();
        }
        _buffer.push(std::move(value));
        if (_buffer.size() == _max_size) {
            _can_produce.store(false);
        }
        _can_consume.store(true);
    }

    T consume() {
        std::unique_lock<std::mutex> lock(_mutex);
        while (_buffer.empty()) {
            lock.unlock();
            std::this_thread::sleep_for(std::chrono::milliseconds(1));
            lock.lock();
        }
        T value = std::move(_buffer.front());
        _buffer.pop();
        if (_buffer.empty()) {
            _can_consume.store(false);
        }
        _can_produce.store(true);
        return value;
    }

    bool isEmpty() const {
        std::lock_guard<std::mutex> lock(_mutex);
        return _buffer.empty();
    }

    bool isFull() const {
        std::lock_guard<std::mutex> lock(_mutex);
        return _buffer.size() >= _max_size;
    }

    size_t size() const {
        std::lock_guard<std::mutex> lock(_mutex);
        return _buffer.size();
    }
};