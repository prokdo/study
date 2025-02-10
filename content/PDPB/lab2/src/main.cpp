#include <iostream>
#include <iomanip>

#include "math/Matrix.hpp"
#include "utils/timer.hpp"

const int FORMAT_METHOD_WIDTH = 30;
const int FORMAT_TIME_WIDTH = 15;
const int FORMAT_MATRIX_SIZE_WIDTH = 20;

void printHeader() {
    std::cout << std::left << std::setw(FORMAT_METHOD_WIDTH) << "Method"
              << std::setw(FORMAT_MATRIX_SIZE_WIDTH) << "Matrix size"
              << std::setw(FORMAT_TIME_WIDTH) << "Time (ms)" << std::endl;
}

void printResult(const std::string& methodName, const std::string& matrixSize, double time) {
    std::cout << std::left << std::setw(FORMAT_METHOD_WIDTH) << methodName
              << std::setw(FORMAT_MATRIX_SIZE_WIDTH) << matrixSize
              << std::setw(FORMAT_TIME_WIDTH) << std::fixed << std::setprecision(3) << time << std::endl;
}

int main() {
    const size_t MIN_SIZE = 100;
    const size_t MAX_SIZE = 500;
    const double MIN_VALUE = 1.0;
    const double MAX_VALUE = 10.0;

    auto generateRandomSize = [](size_t minSize, size_t maxSize) {
        static std::random_device rd;
        static std::mt19937 gen(rd());
        std::uniform_int_distribution<size_t> dis(minSize, maxSize);
        return dis(gen);
    };

    const size_t rows = generateRandomSize(MIN_SIZE, MAX_SIZE);
    const size_t columns = generateRandomSize(MIN_SIZE, MAX_SIZE);

    const std::string matrixSize = std::to_string(rows) + " x " + std::to_string(columns);

    Matrix<double> A(rows, columns);
    A.randomize(MIN_VALUE, MAX_VALUE);

    Timer timer;

    {
        Matrix<double> B(rows, columns);
        B.randomize(MIN_VALUE, MAX_VALUE);

        printHeader();

        std::cout << std::endl;

        timer.start();
        A.sequentialAdd(B);
        timer.stop();
        printResult("Sequential add", matrixSize, timer.elapsedMilliseconds());

        timer.start();
        A.parallelAdd(B);
        timer.stop();
        printResult("Parallel add", matrixSize, timer.elapsedMilliseconds());

        timer.start();
        A.asyncAdd(B);
        timer.stop();
        printResult("Async add", matrixSize, timer.elapsedMilliseconds());

        std::cout << std::endl;

        timer.start();
        A.sequentialSubtract(B);
        timer.stop();
        printResult("Sequential subtract", matrixSize, timer.elapsedMilliseconds());

        timer.start();
        A.parallelSubtract(B);
        timer.stop();
        printResult("Parallel subtract", matrixSize, timer.elapsedMilliseconds());

        timer.start();
        A.asyncSubtract(B);
        timer.stop();
        printResult("Async subtract", matrixSize, timer.elapsedMilliseconds());

        std::cout << std::endl;
    }

    {
        const size_t innerDim = generateRandomSize(MIN_SIZE, MAX_SIZE);

        Matrix<double> B(columns, innerDim);
        B.randomize(MIN_VALUE, MAX_VALUE);

        timer.start();
        A.sequentialMultiply(B);
        timer.stop();
        printResult("Sequential multiply", matrixSize + " x " + std::to_string(innerDim), timer.elapsedMilliseconds());

        timer.start();
        A.parallelMultiply(B);
        timer.stop();
        printResult("Parallel multiply", matrixSize + " x " + std::to_string(innerDim), timer.elapsedMilliseconds());

        timer.start();
        A.asyncMultiply(B);
        timer.stop();
        printResult("Async multiply", matrixSize + " x " + std::to_string(innerDim), timer.elapsedMilliseconds());

        std::cout << std::endl;
    }

    return 0;
}