#include <stdexcept>

#include "math/integration.hpp"

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
    if (n % 2 != 0) {
        throw std::invalid_argument("Number of intervals must be even for Simpson's rule");
    }
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
    if (n % 2 != 0) {
        throw std::invalid_argument("Number of intervals must be even for Simpson's rule");
    }
    float h = (b - a) / n;
    float sum = f(a) + f(b);
    for (int i = 1; i < n; ++i) {
        float x = a + i * h;
        sum += (i % 2 == 0) ? 2 * f(x) : 4 * f(x);
    }
    return sum * h / 3.0f;
}