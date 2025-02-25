#pragma once

#include <condition_variable>
#include <memory>
#include <mutex>
#include <optional>
#include <vector>

template <typename T>
class TSVectorQueue {
private:
    std::vector<T> _data;
    size_t _head = 0;
    size_t _tail = 0;
    mutable std::mutex _mutex;
    std::condition_variable _cond;

public:
    void push(T value) {
        std::lock_guard<std::mutex> lock(_mutex);
        if (_tail == _data.size()) _data.push_back(std::move(value));
        else _data[_tail] = std::move(value);
        ++_tail;
        _cond.notify_one();
    }

    std::optional<T> pop() {
        std::unique_lock<std::mutex> lock(_mutex);
        _cond.wait(lock, [this]() { return _head < _tail; });
        T value = std::move(_data[_head]);
        ++_head;
        return value;
    }

    bool empty() const {
        std::lock_guard<std::mutex> lock(_mutex);
        return _head >= _tail;
    }

    size_t size() const {
        std::lock_guard<std::mutex> lock(_mutex);
        return _tail - _head;
    }

    void clear() {
        std::lock_guard<std::mutex> lock(_mutex);
        _data.clear();
    }
};

template <typename T>
class TSNodeQueue {
private:
    struct _Node {
        T data;
        std::unique_ptr<_Node> next;
        _Node(T value) : data(std::move(value)), next(nullptr) {}
    };

    std::unique_ptr<_Node> _head;
    _Node* _tail;
    mutable std::mutex _mutex;
    std::condition_variable _cond;

public:
    TSNodeQueue() : _head(nullptr), _tail(nullptr) {}

    void push(T value) {
        auto new_node = std::make_unique<_Node>(std::move(value));
        std::lock_guard<std::mutex> lock(_mutex);
        if (!_tail) {
            _head = std::move(new_node);
            _tail = _head.get();
        } else {
            _tail->next = std::move(new_node);
            _tail = _tail->next.get();
        }
        _cond.notify_one();
    }

    std::optional<T> pop() {
        std::unique_lock<std::mutex> lock(_mutex);
        _cond.wait(lock, [this]() { return _head != nullptr; });
        std::optional<T> value = std::move(_head->data);
        _head = std::move(_head->next);
        if (!_head) {
            _tail = nullptr;
        }
        return value;
    }

    bool isEmpty() const {
        std::lock_guard<std::mutex> lock(_mutex);
        return _head == nullptr;
    }

    size_t size() const {
        std::lock_guard<std::mutex> lock(_mutex);
        size_t count = 0;
        auto current = _head.get();
        while (current) {
            ++count;
            current = current->next.get();
        }
        return count;
    }

    void clear() {
        std::lock_guard<std::mutex> lock(_mutex);
        _head.reset();
        _tail = nullptr;
    }
};