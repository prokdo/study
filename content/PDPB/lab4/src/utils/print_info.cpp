#include "utils/print_info.hpp"

#include <iomanip>
#include <iostream>

void printIntegrationBlockHeader() {
    std::cout << std::left << std::setw(INTEGRATION_FORMAT_METHOD_WIDTH) << "Method"
              << " " << std::setw(INTEGRATION_FORMAT_VALUE_WIDTH) << "Value"
              << " " << std::setw(INTEGRATION_FORMAT_TIME_WIDTH) << "Time (ms)" << std::endl
              << std::endl;
}

void printIntegrationBlockResult(const std::string& methodName, const double value, const double time) {
    std::cout << std::left << std::setw(INTEGRATION_FORMAT_METHOD_WIDTH) << methodName
              << " " << std::setw(INTEGRATION_FORMAT_VALUE_WIDTH) << std::fixed
              << std::setprecision(INTEGRATION_FORMAT_PI_PRECISION) << value << " "
              << std::setw(INTEGRATION_FORMAT_TIME_WIDTH) << std::fixed
              << std::setprecision(INTEGRATION_FORMAT_TIME_PRECISION) << time << std::endl;
}

void printSortBlockHeader() {
    std::cout << std::left << std::setw(SORT_FORMAT_METHOD_WIDTH) << "Method"
              << std::setw(SORT_FORMAT_VECTOR_SIZE_WIDTH) << "Vector size"
              << std::setw(SORT_FORMAT_TIME_WIDTH) << "Time (ms)" << std::endl;
}

void printSortBlockResult(const std::string& methodName, const size_t vectorSize, const double time) {
    std::cout << std::left << std::setw(SORT_FORMAT_METHOD_WIDTH) << methodName
              << std::setw(MATRICES_FORMAT_MATRIX_SIZE_WIDTH) << vectorSize
              << std::setw(MATRICES_FORMAT_TIME_WIDTH) << std::fixed << std::setprecision(SORT_FORMAT_TIME_PRECISION) << time << std::endl;
}

void printMatricesBlockHeader() {
    std::cout << std::left << std::setw(MATRICES_FORMAT_METHOD_WIDTH) << "Method"
              << std::setw(MATRICES_FORMAT_MATRIX_SIZE_WIDTH) << "Matrix size"
              << std::setw(MATRICES_FORMAT_TIME_WIDTH) << "Time (ms)" << std::endl;
}

void printMatricesBlockResult(const std::string& methodName, const std::string& matrixSize, const double time) {
    std::cout << std::left << std::setw(MATRICES_FORMAT_METHOD_WIDTH) << methodName
              << std::setw(MATRICES_FORMAT_MATRIX_SIZE_WIDTH) << matrixSize
              << std::setw(MATRICES_FORMAT_TIME_WIDTH) << std::fixed << std::setprecision(MATRICES_FORMAT_TIME_PRECISION) << time << std::endl;
}