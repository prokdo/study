#include "math/integration.hpp"

#include <mpi.h>
#include <stdexcept>

double integrate_rectangles_double(double (*f)(double), double a, double b, int n) {
    double h = (b - a) / n;
    double sum = 0.0;
    for (int i = 0; i < n; ++i) {
        double x = a + i * h;
        sum += f(x);
    }
    return sum * h;
}

double integrate_trapezoids_double(double (*f)(double), double a, double b, int n) {
    double h = (b - a) / n;
    double sum = 0.5 * (f(a) + f(b));
    for (int i = 1; i < n; ++i) {
        double x = a + i * h;
        sum += f(x);
    }
    return sum * h;
}

double integrate_simpson_double(double (*f)(double), double a, double b, int n) {
    if (n % 2 != 0) throw std::invalid_argument("Number of intervals must be even for Simpson's rule");

    double h = (b - a) / n;
    double sum = f(a) + f(b);
    for (int i = 1; i < n; ++i) {
        double x = a + i * h;
        sum += (i % 2 == 0) ? 2 * f(x) : 4 * f(x);
    }
    return sum * h / 3.0;
}

float integrate_rectangles_float(float (*f)(float), float a, float b, int n) {
    float h = (b - a) / n;
    float sum = 0.0f;
    for (int i = 0; i < n; ++i) {
        float x = a + i * h;
        sum += f(x);
    }
    return sum * h;
}

float integrate_trapezoids_float(float (*f)(float), float a, float b, int n) {
    float h = (b - a) / n;
    float sum = 0.5f * (f(a) + f(b));
    for (int i = 1; i < n; ++i) {
        float x = a + i * h;
        sum += f(x);
    }
    return sum * h;
}

float integrate_simpson_float(float (*f)(float), float a, float b, int n) {
    if (n % 2 != 0) throw std::invalid_argument("Number of intervals must be even for Simpson's rule");

    float h = (b - a) / n;
    float sum = f(a) + f(b);
    for (int i = 1; i < n; ++i) {
        float x = a + i * h;
        sum += (i % 2 == 0) ? 2 * f(x) : 4 * f(x);
    }
    return sum * h / 3.0f;
}

double integrate_rectangles_mpi_double(double (*f)(double), double a, double b, int n) {
    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (n <= 0) {
        if (rank == 0) throw std::invalid_argument("Number of intervals must be positive");
        return 0.0;
    }

    double h = (b - a) / n;
    int local_n = n / size;
    int remainder = n % size;

    int start = rank * local_n;
    if (rank < remainder) {
        local_n += 1;
        start += rank;
    } else start += remainder;

    double local_sum = 0.0;
    for (int i = 0; i < local_n; ++i) {
        double x = a + (start + i) * h;
        local_sum += f(x);
    }

    double global_sum;
    MPI_Reduce(&local_sum, &global_sum, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);

    return (rank == 0) ? global_sum * h : 0.0;
}

double integrate_trapezoids_mpi_double(double (*f)(double), double a, double b, int n) {
    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (n <= 0) {
        if (rank == 0) throw std::invalid_argument("Number of intervals must be positive");
        return 0.0;
    }

    double h = (b - a) / n;
    int local_n = n / size;
    int remainder = n % size;

    int start = rank * local_n;
    if (rank < remainder) {
        local_n += 1;
        start += rank;
    } else start += remainder;

    double local_sum = 0.0;
    for (int i = 0; i < local_n; ++i) {
        double x = a + (start + i) * h;
        local_sum += f(x);
    }

    if (rank == 0 && local_n > 0) local_sum += 0.5 * f(a);
    if (rank == size-1 && local_n > 0) local_sum += 0.5 * f(b);

    double global_sum;
    MPI_Reduce(&local_sum, &global_sum, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);

    return (rank == 0) ? global_sum * h : 0.0;
}

double integrate_simpson_mpi_double(double (*f)(double), double a, double b, int n) {
    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (n <= 0) {
        if (rank == 0) throw std::invalid_argument("Number of intervals must be positive");
        return 0.0;
    }

    if (n % 2 != 0) {
        if (rank == 0) throw std::invalid_argument("Number of intervals must be even for Simpson's rule");
        return 0.0;
    }

    double h = (b - a) / n;
    int total_points = n + 1;
    int local_n = total_points / size;
    int remainder = total_points % size;

    int start = rank * local_n;
    if (rank < remainder) {
        local_n += 1;
        start += rank;
    } else start += remainder;

    double local_sum = 0.0;
    for (int i = 0; i < local_n; ++i) {
        int global_i = start + i;
        double x = a + global_i * h;
        if (global_i == 0 || global_i == n) local_sum += f(x);
        else if (global_i % 2 == 0) local_sum += 2 * f(x);
        else local_sum += 4 * f(x);
    }

    double global_sum;
    MPI_Reduce(&local_sum, &global_sum, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);

    return (rank == 0) ? global_sum * h / 3.0 : 0.0;
}

float integrate_rectangles_mpi_float(float (*f)(float), float a, float b, int n) {
    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (n <= 0) {
        if (rank == 0) throw std::invalid_argument("Number of intervals must be positive");
        return 0.0f;
    }

    float h = (b - a) / n;
    int local_n = n / size;
    int remainder = n % size;

    int start = rank * local_n;
    if (rank < remainder) {
        local_n += 1;
        start += rank;
    } else start += remainder;

    float local_sum = 0.0f;
    for (int i = 0; i < local_n; ++i) {
        float x = a + (start + i) * h;
        local_sum += f(x);
    }

    float global_sum;
    MPI_Reduce(&local_sum, &global_sum, 1, MPI_FLOAT, MPI_SUM, 0, MPI_COMM_WORLD);

    return (rank == 0) ? global_sum * h : 0.0f;
}

float integrate_trapezoids_mpi_float(float (*f)(float), float a, float b, int n) {
    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (n <= 0) {
        if (rank == 0) throw std::invalid_argument("Number of intervals must be positive");
        return 0.0;
    }

    float h = (b - a) / n;
    int local_n = n / size;
    int remainder = n % size;

    int start = rank * local_n;
    if (rank < remainder) {
        local_n += 1;
        start += rank;
    } else start += remainder;

    float local_sum = 0.0;
    for (int i = 0; i < local_n; ++i) {
        float x = a + (start + i) * h;
        local_sum += f(x);
    }

    if (rank == 0 && local_n > 0) local_sum += 0.5 * f(a);
    if (rank == size-1 && local_n > 0) local_sum += 0.5 * f(b);

    float global_sum;
    MPI_Reduce(&local_sum, &global_sum, 1, MPI_FLOAT, MPI_SUM, 0, MPI_COMM_WORLD);

    return (rank == 0) ? global_sum * h : 0.0;
}

float integrate_simpson_mpi_float(float (*f)(float), float a, float b, int n) {
    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (n <= 0) {
        if (rank == 0) throw std::invalid_argument("Number of intervals must be positive");
        return 0.0;
    }

    if (n % 2 != 0) {
        if (rank == 0) throw std::invalid_argument("Number of intervals must be even for Simpson's rule");
        return 0.0;
    }

    float h = (b - a) / n;
    int total_points = n + 1;
    int local_n = total_points / size;
    int remainder = total_points % size;

    int start = rank * local_n;
    if (rank < remainder) {
        local_n += 1;
        start += rank;
    } else start += remainder;

    float local_sum = 0.0;
    for (int i = 0; i < local_n; ++i) {
        int global_i = start + i;
        float x = a + global_i * h;
        if (global_i == 0 || global_i == n) local_sum += f(x);
        else if (global_i % 2 == 0) local_sum += 2 * f(x);
        else local_sum += 4 * f(x);
    }

    float global_sum;
    MPI_Reduce(&local_sum, &global_sum, 1, MPI_FLOAT, MPI_SUM, 0, MPI_COMM_WORLD);

    return (rank == 0) ? global_sum * h / 3.0 : 0.0;
}

double pi_integrand_double(double x) {
    return 4.0 / (1.0 + x * x);
}

float pi_integrand_float(float x) {
    return 4.0f / (1.0f + x * x);
}