#include "demo/matrices_demo.hpp"

#include <random>

#include "math/Matrix.hpp"
#include "utils/print_info.hpp"
#include "utils/timer.hpp"

void matrices_demo() {
    auto generateRandomSize = [](size_t minSize, size_t maxSize) {
        static std::random_device rd;
        static std::mt19937 gen(rd());
        std::uniform_int_distribution<size_t> dis(minSize, maxSize);
        return dis(gen);
    };

    const size_t rows = generateRandomSize(MATRIX_MIN_SIZE, MATRIX_MAX_SIZE);
    const size_t columns = generateRandomSize(MATRIX_MIN_SIZE, MATRIX_MIN_SIZE);

    const std::string matrixSize = std::to_string(rows) + " x " + std::to_string(columns);

    Matrix<double> A(rows, columns);
    A.randomize(MATRIX_MIN_VALUE, MATRIX_MAX_VALUE);

    std::cout << "--- Matrices ---" << std::endl;

    Timer timer;

    {
        Matrix<double> B(rows, columns);
        B.randomize(MATRIX_MIN_VALUE, MATRIX_MAX_VALUE);

        printMatricesBlockHeader();

        std::cout << std::endl;

        timer.start();
        A.addSequential(B);
        timer.stop();
        printMatricesBlockResult("Sequential add", matrixSize, timer.elapsedMilliseconds());

        timer.start();
        A.addOMP(B);
        timer.stop();
        printMatricesBlockResult("OpenMP add", matrixSize, timer.elapsedMilliseconds());

        std::cout << std::endl;

        timer.start();
        A.subtractSequential(B);
        timer.stop();
        printMatricesBlockResult("Sequential subtract", matrixSize, timer.elapsedMilliseconds());

        timer.start();
        A.subtractOMP(B);
        timer.stop();
        printMatricesBlockResult("OpenMP subtract", matrixSize, timer.elapsedMilliseconds());

        std::cout << std::endl;
    }

    {
        const size_t innerDim = generateRandomSize(MATRIX_MIN_SIZE, MATRIX_MAX_SIZE);

        Matrix<double> B(columns, innerDim);
        B.randomize(MATRIX_MIN_VALUE, MATRIX_MAX_VALUE);

        timer.start();
        A.multiplySequential(B);
        timer.stop();
        printMatricesBlockResult("Sequential multiply", matrixSize + " x " + std::to_string(innerDim), timer.elapsedMilliseconds());

        timer.start();
        A.multiplyOMP(B);
        timer.stop();
        printMatricesBlockResult("OpenMP multiply", matrixSize + " x " + std::to_string(innerDim), timer.elapsedMilliseconds());

        std::cout << std::endl;
    }
}