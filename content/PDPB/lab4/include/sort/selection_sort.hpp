#pragma once

#include <vector>
#include <omp.h>

template <typename T>
void selection_sort_sequential(std::vector<T>& arr) {
    for (size_t i = 0; i < arr.size() - 1; ++i) {
        size_t min_idx = i;
        for (size_t j = i + 1; j < arr.size(); ++j)
            if (arr[j] < arr[min_idx]) min_idx = j;

        std::swap(arr[i], arr[min_idx]);
    }
}

template <typename T>
void selection_sort_omp(std::vector<T>& arr, int num_threads) {
    const size_t n = arr.size();
    omp_set_num_threads(num_threads);

    for (size_t i = 0; i < n - 1; ++i) {
        size_t min_idx = i;

        #pragma omp parallel
        {
            size_t local_min = i;
            #pragma omp for
            for (size_t j = i + 1; j < n; ++j)
                if (arr[j] < arr[local_min]) local_min = j;

            #pragma omp critical
            { if (arr[local_min] < arr[min_idx]) min_idx = local_min; }
        }

        std::swap(arr[i], arr[min_idx]);
    }
}