#pragma once

#include <cstddef>
#include <fstream>
#include <iostream>
#include <stdexcept>
#include <vector>
#include <random>
#include <tuple>
#include <type_traits>

template <typename T>
class Matrix {
    static_assert(std::is_arithmetic_v<T>, "Matrix can only be used with numeric types");

private:
    std::vector<std::vector<T>> _data;
    size_t _rows, _columns;

public:
    Matrix(size_t r, size_t c)
        : _rows(r), _columns(c), _data(r, std::vector<T>(c)) {}

    Matrix(const std::vector<std::vector<T>>& init)
        : _rows(init.size()), _columns(init[0].size()), _data(init) {}

    Matrix(const Matrix& other)
        : _rows(other._rows), _columns(other._columns), _data(other._data) {}

    Matrix(Matrix&& other) noexcept
        : _rows(other._rows), _columns(other._columns), _data(std::move(other._data)) {
        other._rows = other._columns = 0;
    }

    explicit Matrix(const std::string& filename) {
        std::ifstream file(filename);
        if (!file.is_open()) throw std::runtime_error("Failed to open file: " + filename);

        file >> _rows >> _columns;
        if (_rows == 0 || _columns == 0) throw std::runtime_error("Invalid matrix dimensions in file");

        _data.resize(_rows, std::vector<T>(_columns));
        for (size_t i = 0; i < _rows; ++i)
            for (size_t j = 0; j < _columns; ++j)
                if (!(file >> _data[i][j])) throw std::runtime_error("Error reading matrix data from file");

        file.close();
    }

    T& operator()(size_t i, size_t j) {
        checkIndices(i, j);

        return _data[i][j];
    }

    const T& operator()(size_t i, size_t j) const {
        checkIndices(i, j);

        return _data[i][j];
    }

    const std::vector<T>& operator()(size_t i) const {
        checkRow(i);

        return _data[i];
    }

    Matrix& operator=(const Matrix& other) {
        if (this != &other) {
            _rows = other._rows;
            _columns = other._columns;
            _data = other._data;
        }

        return *this;
    }

    Matrix& operator=(Matrix&& other) noexcept {
        if (this != &other) {
            _rows = other._rows;
            _columns = other._columns;
            _data = std::move(other._data);
            other._rows = other._columns = 0;
        }

        return *this;
    }

    size_t rows() const { return _rows; }
    size_t columns() const { return _columns; }
    std::tuple<size_t, size_t> size() const { return {_rows, _columns}; }
    bool isEmpty() const { return _rows == 0 || _columns == 0; }

    void randomize(T min = 0, T max = 1) {
        static_assert(std::is_arithmetic_v<T>, "Randomization requires numeric type");

        std::random_device rd;
        if constexpr (std::is_floating_point_v<T>) {
            std::mt19937 gen(rd());
            std::uniform_real_distribution<T> dis(min, max);
            #pragma omp parallel for
            for (size_t i = 0; i < _rows; ++i)
                for (size_t j = 0; j < _columns; ++j) _data[i][j] = dis(gen);

        } else {
            std::mt19937 gen(rd());
            std::uniform_int_distribution<T> dis(min, max);
            #pragma omp parallel for
            for (size_t i = 0; i < _rows; ++i)
                for (size_t j = 0; j < _columns; ++j) _data[i][j] = dis(gen);
        }
    }

    Matrix addSequential(const Matrix& other) const {
        validateDimensions(other);
        Matrix result(_rows, _columns);

        for (size_t i = 0; i < _rows; ++i)
            for (size_t j = 0; j < _columns; ++j) result(i, j) = _data[i][j] + other(i, j);

        return result;
    }

    Matrix addOMP(const Matrix& other) const {
        validateDimensions(other);
        Matrix result(_rows, _columns);

        #pragma omp parallel for
        for (size_t i = 0; i < _rows; ++i)
            for (size_t j = 0; j < _columns; ++j) result(i, j) = _data[i][j] + other(i, j);


        return result;
    }

    Matrix subtractSequential(const Matrix& other) const {
        validateDimensions(other);
        Matrix result(_rows, _columns);

        for (size_t i = 0; i < _rows; ++i)
            for (size_t j = 0; j < _columns; ++j) result(i, j) = _data[i][j] - other(i, j);

        return result;
    }

    Matrix subtractOMP(const Matrix& other) const {
        validateDimensions(other);
        Matrix result(_rows, _columns);

        #pragma omp parallel for
        for (size_t i = 0; i < _rows; ++i)
            for (size_t j = 0; j < _columns; ++j) result(i, j) = _data[i][j] - other(i, j);

        return result;
    }

    Matrix multiplySequential(const Matrix& other) const {
        validateMultiplication(other);
        Matrix result(_rows, other.columns());

        for (size_t i = 0; i < _rows; ++i)
            for (size_t k = 0; k < _columns; ++k) {
                const T temp = _data[i][k];
                for (size_t j = 0; j < other.columns(); ++j) result(i, j) += temp * other(k, j);
            }

        return result;
    }

    Matrix multiplyOMP(const Matrix& other) const {
        validateMultiplication(other);
        Matrix result(_rows, other.columns());

        #pragma omp parallel for
        for (size_t i = 0; i < _rows; ++i) {
            std::vector<T> rowResult(other.columns(), 0);
            for (size_t k = 0; k < _columns; ++k) {
                const T temp = _data[i][k];
                for (size_t j = 0; j < other.columns(); ++j) rowResult[j] += temp * other(k, j);
            }

            #pragma omp critical
            { for (size_t j = 0; j < other.columns(); ++j) result(i, j) += rowResult[j]; }
        }

        return result;
    }

    friend std::ostream& operator<<(std::ostream& os, const Matrix& matrix) {
        for (size_t i = 0; i < matrix.rows(); ++i) {
            for (size_t j = 0; j < matrix.columns(); ++j) os << matrix(i, j) << " ";
            os << "\n";
        }

        return os;
    }

private:
    void checkIndices(size_t i, size_t j) const {
        if (i >= _rows || j >= _columns)
            throw std::out_of_range("Index (" + std::to_string(i) +
                                    "," + std::to_string(j) + ") out of range");
    }

    void checkRow(size_t i) const {
        if (i >= _rows) throw std::out_of_range("Row index " + std::to_string(i) + " out of range");
    }

    void validateDimensions(const Matrix& other) const {
        if (_rows != other.rows() || _columns != other.columns()) throw std::invalid_argument("Matrix dimensions do not match");
    }

    void validateMultiplication(const Matrix& other) const {
        if (_columns != other.rows()) throw std::invalid_argument("Columns/rows mismatch for multiplication");
    }
};