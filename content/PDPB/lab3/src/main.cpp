#include <iostream>
#include <iomanip>
#include <random>
#include <vector>
#include <thread>
#include "struct/ts_queue.hpp"
#include "scheme/producer_consumer.hpp"
#include "scheme/reader_writer.hpp"
#include "scheme/barrier.hpp"
#include "utils/timer.hpp"

const int FORMAT_OPERATION_WIDTH = 50;
const int FORMAT_TIME_WIDTH = 15;

void printHeader() {
    std::cout << std::left << std::setw(FORMAT_OPERATION_WIDTH) << "Operation"
              << std::setw(FORMAT_TIME_WIDTH) << "Time (ms)" << std::endl;
}

void printResult(const std::string& operation, double time) {
    std::cout << std::left << std::setw(FORMAT_OPERATION_WIDTH) << operation
              << std::setw(FORMAT_TIME_WIDTH) << std::fixed << std::setprecision(3) << time << std::endl;
}

int main() {
    const size_t MIN_SIZE = 10000;
    const size_t MAX_SIZE = 50000;
    const size_t MAX_THREADS = 4;

    auto generateRandomSize = [](size_t minSize, size_t maxSize) {
        static std::random_device rd;
        static std::mt19937 gen(rd());
        std::uniform_int_distribution<size_t> dis(minSize, maxSize);
        return dis(gen);
    };

    const size_t dataSize = generateRandomSize(MIN_SIZE, MAX_SIZE);
    std::vector<int> data(dataSize, 1);
    std::atomic<int> result;
    Timer timer;

    printHeader();

    {
        result = 0;
        std::cout << "\n--- Sequential vector ---\n";
        timer.start();
        for (size_t i = 0; i < dataSize; ++i) data[i] = 1;
        for (const auto& value : data) result += value;
        timer.stop();
        printResult("Production & Consumption", timer.elapsedMilliseconds());
        std::cout << "Result: " << result << std::endl;
    }

    {
        result = 0;
        TSVectorQueue<int> queue;
        std::cout << "\n--- Thread-safe vector queue ---\n";

        size_t num_producers = MAX_THREADS / 2;
        size_t num_consumers = MAX_THREADS - num_producers;

        size_t rem = dataSize % MAX_THREADS;

        timer.start();
        std::vector<std::thread> producers;
        for (size_t i = 0; i < num_producers; ++i)
            producers.emplace_back([&queue, &data, dataSize, num_producers, rem]() {
                for (size_t j = 0; j < (dataSize - rem) / num_producers; ++j) queue.push(data[j]);
            });

        std::vector<std::thread> consumers;
        for (size_t i = 0; i < num_consumers; ++i)
            producers.emplace_back([&queue, dataSize, num_consumers, &result, rem]() {
                for (size_t j = 0; j < (dataSize - rem) / num_consumers; ++j) result += queue.pop().value_or(result += 0);
            });

        for (auto& producer: producers) producer.join();
        for (auto& consumer: consumers) consumer.join();

        for (size_t i = dataSize - rem; i < dataSize; ++i) result += data[i];

        timer.stop();

        printResult("Production & Consumption", timer.elapsedMilliseconds());
        std::cout << "Result: " << result << std::endl;
    }

    {
        result = 0;
        TSNodeQueue<int> queue;
        std::cout << "\n--- Thread-safe node queue ---\n";

        size_t num_producers = MAX_THREADS / 2;
        size_t num_consumers = MAX_THREADS - num_producers;

        size_t rem = dataSize % MAX_THREADS;

        timer.start();
        std::vector<std::thread> producers;
        for (size_t i = 0; i < num_producers; ++i)
            producers.emplace_back([&queue, &data, dataSize, num_producers, rem]() {
                for (size_t j = 0; j < (dataSize - rem) / num_producers; ++j) queue.push(data[j]);
            });

        std::vector<std::thread> consumers;
        for (size_t i = 0; i < num_consumers; ++i)
            producers.emplace_back([&queue, dataSize, num_consumers, &result, rem]() {
                for (size_t j = 0; j < (dataSize - rem) / num_consumers; ++j) result += queue.pop().value_or(result += 0);
            });

        for (auto& producer: producers) producer.join();
        for (auto& consumer: consumers) consumer.join();

        for (size_t i = dataSize - rem; i < dataSize; ++i) result += data[i];

        timer.stop();

        printResult("Production & Consumption", timer.elapsedMilliseconds());
        std::cout << "Result: " << result << std::endl;
    }

    {
        result = 0;
        ProducerConsumerConditional<int> pc(100);
        std::cout << "\n--- Producer & Consumer (conditional variables) scheme ---\n";

        size_t num_producers = MAX_THREADS / 2;
        size_t num_consumers = MAX_THREADS - num_producers;

        size_t rem = dataSize % MAX_THREADS;

        timer.start();
        std::vector<std::thread> producers;
        for (size_t i = 0; i < num_producers; ++i)
            producers.emplace_back([&pc, &data, dataSize, num_producers, rem]() {
                for (size_t j = 0; j < (dataSize - rem) / num_producers; ++j) pc.produce(data[j]);
            });

        std::vector<std::thread> consumers;
        for (size_t i = 0; i < num_consumers; ++i)
            producers.emplace_back([&pc, dataSize, num_consumers, &result, rem]() {
                for (size_t j = 0; j < (dataSize - rem) / num_consumers; ++j) result += pc.consume();
            });

        for (auto& producer: producers) producer.join();
        for (auto& consumer: consumers) consumer.join();

        for (size_t i = dataSize - rem; i < dataSize; ++i) result += data[i];

        timer.stop();

        printResult("Production & Consumption", timer.elapsedMilliseconds());
        std::cout << "Result: " << result << std::endl;
    }

    {
        result = 0;
        ProducerConsumerAtomic<int> pc(100);

        std::cout << "\n--- Producer & Consumer (atomic variables) scheme ---\n";

        size_t num_producers = MAX_THREADS / 2;
        size_t num_consumers = MAX_THREADS - num_producers;

        size_t rem = dataSize % MAX_THREADS;

        timer.start();
        std::vector<std::thread> producers;
        for (size_t i = 0; i < num_producers; ++i)
            producers.emplace_back([&pc, &data, dataSize, num_producers, rem]() {
                for (size_t j = 0; j < (dataSize - rem) / num_producers; ++j) pc.produce(data[j]);
            });

        std::vector<std::thread> consumers;
        for (size_t i = 0; i < num_consumers; ++i)
            producers.emplace_back([&pc, dataSize, num_consumers, &result, rem]() {
                for (size_t j = 0; j < (dataSize - rem) / num_consumers; ++j) result += pc.consume();
            });

        for (auto& producer: producers) producer.join();
        for (auto& consumer: consumers) consumer.join();

        for (size_t i = dataSize - rem; i < dataSize; ++i) result += data[i];

        timer.stop();

        printResult("Production & Consumption", timer.elapsedMilliseconds());
        std::cout << "Result: " << result << std::endl;
    }

    {
        result = 0;
        ReaderWriter<int> rw(0);

        std::cout << "\n--- Writer & Reader scheme ---\n";

        size_t num_producers = MAX_THREADS / 2;
        size_t num_consumers = MAX_THREADS - num_producers;

        size_t rem = dataSize % MAX_THREADS;

        timer.start();
        std::vector<std::thread> producers;
        for (size_t i = 0; i < num_producers; ++i)
            producers.emplace_back([&rw, &data, dataSize, num_producers, rem]() {
                for (size_t j = 0; j < (dataSize - rem) / num_producers; ++j) rw.write(data[j]);
            });

        std::vector<std::thread> consumers;
        for (size_t i = 0; i < num_consumers; ++i)
            producers.emplace_back([&rw, dataSize, num_consumers, &result, rem]() {
                for (size_t j = 0; j < (dataSize - rem) / num_consumers; ++j) result += rw.read();
            });

        for (auto& producer: producers) producer.join();
        for (auto& consumer: consumers) consumer.join();

        for (size_t i = dataSize - rem; i < dataSize; ++i) result += data[i];

        timer.stop();

        printResult("Write & Read", timer.elapsedMilliseconds());
        std::cout << "Result: " << result << std::endl;
    }

    {
        std::cout << "\n--- Parallel reduction scheme ---\n";
        std::vector<int> data(dataSize, 1);

        timer.start();
        result = parallel_reduce(data, MAX_THREADS, std::plus<>());
        timer.stop();

        printResult("Parallel reduction", timer.elapsedMilliseconds());
        std::cout << "Result: " << result << std::endl;
    }

    return 0;
}