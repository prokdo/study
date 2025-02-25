#pragma once

#include <condition_variable>
#include <mutex>
#include <numeric>
#include <thread>
#include <vector>

class Barrier {
private:
    size_t _count;
    size_t _current;
    size_t _phase;
    std::mutex _mutex;
    std::condition_variable _cond;

public:
    explicit Barrier(size_t count);

    void wait();

    void reset(size_t count = 0);
};

template<typename T, typename BinaryOperation>
T parallel_reduce(const std::vector<T>& data, size_t max_threads, BinaryOperation op) {
    size_t num_threads = std::min(max_threads, data.size());
    if (num_threads == 0) return T();

    std::vector<T> partial_results(num_threads);
    Barrier barrier(num_threads);
    auto reduce_task = [&](size_t id) {
        size_t chunk_size = data.size() / num_threads;
        size_t start = id * chunk_size;
        size_t end = (id == num_threads - 1) ? data.size() : start + chunk_size;
        partial_results[id] = std::accumulate(data.begin() + start, data.begin() + end, T());
        barrier.wait();
        if (id == 0) {
            T result = partial_results[0];
            for (size_t i = 1; i < num_threads; ++i) result = op(result, partial_results[i]);

            partial_results[0] = result;
        }
        barrier.wait();
    };
    std::vector<std::thread> threads;
    for (size_t i = 0; i < num_threads; ++i) threads.emplace_back(reduce_task, i);

    for (auto& t : threads) t.join();

    return partial_results[0];
}