#pragma once

#include <cstddef>
#include <fstream>
#include <future>
#include <iostream>
#include <vector>
#include <random>
#include <thread>
#include <tuple>
#include <type_traits>

template <typename T>
class Matrix {
    static_assert(std::is_arithmetic<T>::value, "Matrix can only be used with numeric types");

private:
    std::vector<std::vector<T>> __data;
    size_t __rows, __columns;

public:
    Matrix() : __rows(0), __columns(0) {}
    Matrix(size_t r, size_t c) : __rows(r), __columns(c), __data(r, std::vector<T>(c, 0)) {}
    Matrix(Matrix&& other) noexcept : __rows(other.__rows), __columns(other.__columns), __data(std::move(other.__data)) {
        other.__rows = 0;
        other.__columns = 0;
    }

    explicit Matrix(const std::string& filename) {
        std::ifstream file(filename);
        if (!file.is_open()) {
            throw std::runtime_error("Failed to open file: " + filename);
        }

        file >> __rows >> __columns;
        if (__rows == 0 || __columns == 0) {
            throw std::runtime_error("Invalid matrix dimensions in file");
        }

        __data.resize(__rows, std::vector<T>(__columns));

        for (size_t i = 0; i < __rows; ++i) {
            for (size_t j = 0; j < __columns; ++j) {
                if (!(file >> __data[i][j])) {
                    throw std::runtime_error("Error reading matrix data from file");
                }
            }
        }

        file.close();
    }

    void randomize(T min = 0, T max = 1) {
        static_assert(std::is_floating_point<T>::value || std::is_integral<T>::value,
                      "Randomization is supported only for numeric types");
        std::random_device rd;
        if constexpr (std::is_floating_point<T>::value) {
            std::mt19937 gen(rd());
            std::uniform_real_distribution<T> dis(min, max);
            for (size_t i = 0; i < __rows; ++i) {
                for (size_t j = 0; j < __columns; ++j) {
                    __data[i][j] = dis(gen);
                }
            }
        } else if constexpr (std::is_integral<T>::value) {
            std::mt19937 gen(rd());
            std::uniform_int_distribution<T> dis(min, max);
            for (size_t i = 0; i < __rows; ++i) {
                for (size_t j = 0; j < __columns; ++j) {
                    __data[i][j] = dis(gen);
                }
            }
        }
    }

    size_t rows() const { return __rows; }
    size_t columns() const { return __columns; }
    std::tuple<size_t, size_t> size() const { return std::make_tuple(__rows, __columns); }

    Matrix& operator=(const Matrix& other) {
        if (this != &other) {
            __rows = other.__rows;
            __columns = other.__columns;
            __data = other.__data;
        }
        return *this;
    }

    Matrix& operator=(Matrix&& other) noexcept {
        if (this != &other) {
            __rows = other.__rows;
            __columns = other.__columns;
            __data = std::move(other.__data);
        }
        return *this;
    }

    bool isEmpty() const {
        return __rows == 0 || __columns == 0;
    }

    Matrix sequentialAdd(const Matrix& other) const {
        if (isEmpty() || other.isEmpty()) {
            throw std::invalid_argument("One or both matrices are empty");
        }

        if (__rows != other.__rows || __columns != other.__columns) {
            throw std::invalid_argument("Matrices dimensions do not match for addition");
        }
        Matrix result(__rows, __columns);
        for (size_t i = 0; i < __rows; ++i) {
            for (size_t j = 0; j < __columns; ++j) {
                result.__data[i][j] = __data[i][j] + other.__data[i][j];
            }
        }
        return result;
    }

    Matrix parallelAdd(const Matrix& other) const {
        if (isEmpty() || other.isEmpty()) {
            throw std::invalid_argument("One or both matrices are empty");
        }

        if (__rows != other.__rows || __columns != other.__columns) {
            throw std::invalid_argument("Matrices dimensions do not match for addition");
        }

        Matrix result(__rows, __columns);
        std::vector<std::thread> threads;

        for (size_t i = 0; i < __rows; ++i) {
            threads.emplace_back([this, &other, &result, i]() {
                for (size_t j = 0; j < __columns; ++j) {
                    result.__data[i][j] = __data[i][j] + other.__data[i][j];
                }
            });
        }

        for (auto& t : threads) {
            t.join();
        }

        return result;
    }

    Matrix asyncAdd(const Matrix& other) const {
        if (isEmpty() || other.isEmpty()) {
            throw std::invalid_argument("One or both matrices are empty");
        }

        if (__rows != other.__rows || __columns != other.__columns) {
            throw std::invalid_argument("Matrices dimensions do not match for addition");
        }

        Matrix result(__rows, __columns);
        std::vector<std::future<void>> futures;

        for (size_t i = 0; i < __rows; ++i) {
            futures.emplace_back(std::async(std::launch::async, [this, &other, &result, i]() {
                for (size_t j = 0; j < __columns; ++j) {
                    result.__data[i][j] = __data[i][j] + other.__data[i][j];
                }
            }));
        }

        for (auto& f : futures) {
            f.wait();
        }

        return result;
    }

    Matrix sequentialSubtract(const Matrix& other) const {
        if (isEmpty() || other.isEmpty()) {
            throw std::invalid_argument("One or both matrices are empty");
        }

        if (__rows != other.__rows || __columns != other.__columns) {
            throw std::invalid_argument("Matrices dimensions do not match for subtraction");
        }
        Matrix result(__rows, __columns);
        for (size_t i = 0; i < __rows; ++i) {
            for (size_t j = 0; j < __columns; ++j) {
                result.__data[i][j] = __data[i][j] - other.__data[i][j];
            }
        }
        return result;
    }

    Matrix parallelSubtract(const Matrix& other) const {
        if (isEmpty() || other.isEmpty()) {
            throw std::invalid_argument("One or both matrices are empty");
        }

        Matrix result(__rows, __columns);
        std::vector<std::thread> threads;

        for (size_t i = 0; i < __rows; ++i) {
            threads.emplace_back([this, &other, &result, i]() {
                for (size_t j = 0; j < __columns; ++j) {
                    result.__data[i][j] = __data[i][j] - other.__data[i][j];
                }
            });
        }

        for (auto& t : threads) {
            t.join();
        }

        return result;
    }

    Matrix asyncSubtract(const Matrix& other) const {
        if (isEmpty() || other.isEmpty()) {
            throw std::invalid_argument("One or both matrices are empty");
        }

        if (__rows != other.__rows || __columns != other.__columns) {
            throw std::invalid_argument("Matrices dimensions do not match for subtraction");
        }

        Matrix result(__rows, __columns);
        std::vector<std::future<void>> futures;

        for (size_t i = 0; i < __rows; ++i) {
            futures.emplace_back(std::async(std::launch::async, [this, &other, &result, i]() {
                for (size_t j = 0; j < __columns; ++j) {
                    result.__data[i][j] = __data[i][j] - other.__data[i][j];
                }
            }));
        }

        for (auto& f : futures) {
            f.wait();
        }

        return result;
    }

    Matrix sequentialMultiply(const Matrix& other) const {
        if (isEmpty() || other.isEmpty()) {
            throw std::invalid_argument("One or both matrices are empty");
        }

        if (__columns != other.__rows) {
            throw std::invalid_argument("Matrices dimensions do not match for multiplication");
        }
        Matrix result(__rows, other.__columns);
        for (size_t i = 0; i < __rows; ++i) {
            for (size_t j = 0; j < other.__columns; ++j) {
                for (size_t k = 0; k < __columns; ++k) {
                    result.__data[i][j] += __data[i][k] * other.__data[k][j];
                }
            }
        }
        return result;
    }

    Matrix parallelMultiply(const Matrix& other) const {
        if (isEmpty() || other.isEmpty()) {
            throw std::invalid_argument("One or both matrices are empty");
        }

        if (__columns != other.__rows) {
            throw std::invalid_argument("Matrices dimensions do not match for multiplication");
        }

        Matrix result(__rows, other.__columns);
        std::vector<std::thread> threads;

        for (size_t i = 0; i < __rows; ++i) {
            threads.emplace_back([this, &other, &result, i]() {
                for (size_t j = 0; j < other.__columns; ++j) {
                    T sum = 0;
                    for (size_t k = 0; k < __columns; ++k) {
                        sum += __data[i][k] * other.__data[k][j];
                    }
                    result.__data[i][j] = sum;
                }
            });
        }

        for (auto& t : threads) {
            t.join();
        }

        return result;
    }

    Matrix asyncMultiply(const Matrix& other) const {
        if (isEmpty() || other.isEmpty()) {
            throw std::invalid_argument("One or both matrices are empty");
        }

        if (__columns != other.__rows) {
            throw std::invalid_argument("Matrices dimensions do not match for multiplication");
        }

        Matrix result(__rows, other.__columns);
        std::vector<std::future<void>> futures;

        for (size_t i = 0; i < __rows; ++i) {
            futures.emplace_back(std::async(std::launch::async, [this, &other, &result, i]() {
                for (size_t j = 0; j < other.__columns; ++j) {
                    T sum = 0;
                    for (size_t k = 0; k < __columns; ++k) {
                        sum += __data[i][k] * other.__data[k][j];
                    }
                    result.__data[i][j] = sum;
                }
            }));
        }

        for (auto& f : futures) {
            f.wait();
        }

        return result;
    }

    friend std::ostream& operator<<(std::ostream& os, const Matrix<T>& matrix) {
        for (size_t i = 0; i < matrix.__rows; ++i) {
            for (size_t j = 0; j < matrix.__columns; ++j) {
                os << matrix.__data[i][j] << " ";
            }
            os << std::endl;
        }
        return os;
    }
};