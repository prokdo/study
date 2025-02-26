#include "demo/sort_demo.hpp"

#include <algorithm>
#include <iostream>
#include <random>

#include "sort/selection_sort.hpp"
#include "utils/print_info.hpp"
#include "utils/timer.hpp"

void sort_demo() {
    std::cout << "--- Sort ---" << std::endl << std::endl;

    auto generateRandomSize = [](size_t minSize, size_t maxSize) {
        static std::random_device rd;
        static std::mt19937 gen(rd());
        std::uniform_int_distribution<size_t> dis(minSize, maxSize);
        return dis(gen);
    };

    auto generateRandomValue = []() {
        static std::random_device rd;
        static std::mt19937 gen(rd());
        std::uniform_int_distribution<size_t> dis(VECTOR_MIN_VALUE, VECTOR_MIN_VALUE);
        return dis(gen);
    };

    const size_t vectorSize = generateRandomSize(VECTOR_MIN_SIZE, VECTOR_MAX_SIZE);
    std::vector<int> vector(vectorSize);
    std::generate(vector.begin(), vector.end(), generateRandomValue);

    std::cout << "N = " << vectorSize << std::endl << std::endl;

    Timer timer;

    printSortBlockHeader();

    {
        std::vector<int> sequential_sort_vector(vector);

        std::cout << std::endl;

        timer.start();
        selection_sort_sequential(sequential_sort_vector);
        timer.stop();

        printSortBlockResult("Sequential sort", vectorSize, timer.elapsedMilliseconds());

    }

    {
        std::vector<int> omp_sort_vector(vector);

        timer.start();
        selection_sort_omp(omp_sort_vector, MAX_THREADS);
        timer.stop();

        printSortBlockResult("OpenMP sort", vectorSize, timer.elapsedMilliseconds());

        std::cout << std::endl;
    }
}