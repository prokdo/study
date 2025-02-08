#include <stdexcept>

#include "math/simd_integration.hpp"

double integrate_simd_rectangles_double(__m256d (*f_simd)(__m256d), double (*f)(double), double a, double b, int n) {
    double h = (b - a) / n;
    __m256d sum_vec = _mm256_setzero_pd();
    int i = 0;

    __m256d h_vec = _mm256_set1_pd(h);
    __m256d indices = _mm256_set_pd(3, 2, 1, 0);

    for (; i <= n - 4; i += 4) {
        __m256d x_base = _mm256_set1_pd(a + i * h);
        __m256d x_vec = _mm256_add_pd(_mm256_mul_pd(indices, h_vec), x_base);

        __m256d f_vec = f_simd(x_vec);

        sum_vec = _mm256_add_pd(sum_vec, f_vec);
    }

    double sum = 0.0;
    for (; i < n; ++i) {
        double x = a + i * h;
        sum += f(x);
    }

    alignas(32) double temp[4];
    _mm256_storeu_pd(temp, sum_vec);
    sum += temp[0] + temp[1] + temp[2] + temp[3];

    return sum * h;
}

double integrate_simd_trapezoids_double(__m256d (*f_simd)(__m256d), double (*f)(double), double a, double b, int n) {
    double h = (b - a) / n;
    __m256d sum_vec = _mm256_setzero_pd();
    int i = 1;

    double sum = 0.5 * (f(a) + f(b));

    __m256d h_vec = _mm256_set1_pd(h);
    __m256d indices = _mm256_set_pd(3, 2, 1, 0);

    for (; i <= n - 4; i += 4) {
        __m256d x_base = _mm256_set1_pd(a + i * h);
        __m256d x_vec = _mm256_add_pd(_mm256_mul_pd(indices, h_vec), x_base);

        __m256d f_vec = f_simd(x_vec);

        sum_vec = _mm256_add_pd(sum_vec, f_vec);
    }

    for (; i < n; ++i) {
        double x = a + i * h;
        sum += f(x);
    }

    alignas(32) double temp[4];
    _mm256_storeu_pd(temp, sum_vec);
    sum += temp[0] + temp[1] + temp[2] + temp[3];

    return sum * h;
}

double integrate_simd_simpson_double(__m256d (*f_simd)(__m256d), double (*f)(double), double a, double b, int n) {
    if (n % 2 != 0) {
        throw std::invalid_argument("Number of intervals must be even for Simpson's rule");
    }
    double h = (b - a) / n;
    __m256d sum_vec = _mm256_setzero_pd();
    int i = 1;

    double sum = f(a) + f(b);

    __m256d h_vec = _mm256_set1_pd(h);
    __m256d indices = _mm256_set_pd(3, 2, 1, 0);

    for (; i <= n - 4; i += 4) {
        __m256d x_base = _mm256_set1_pd(a + i * h);
        __m256d x_vec = _mm256_add_pd(_mm256_mul_pd(indices, h_vec), x_base);

        __m256d f_vec = f_simd(x_vec);

        __m256d coeff_vec = _mm256_set_pd(
            (i + 3) % 2 == 0 ? 2.0 : 4.0,
            (i + 2) % 2 == 0 ? 2.0 : 4.0,
            (i + 1) % 2 == 0 ? 2.0 : 4.0,
            i % 2 == 0 ? 2.0 : 4.0
        );

        f_vec = _mm256_mul_pd(f_vec, coeff_vec);

        sum_vec = _mm256_add_pd(sum_vec, f_vec);
    }

    for (; i < n; ++i) {
        double x = a + i * h;
        sum += (i % 2 == 0) ? 2 * f(x) : 4 * f(x);
    }

    alignas(32) double temp[4];
    _mm256_storeu_pd(temp, sum_vec);
    sum += temp[0] + temp[1] + temp[2] + temp[3];

    return sum * h / 3.0;
}

float integrate_simd_rectangles_float(__m256 (*f_simd)(__m256), float (*f)(float), float a, float b, int n) {
    float h = (b - a) / n;
    __m256 sum_vec = _mm256_setzero_ps();
    int i = 0;

    __m256 h_vec = _mm256_set1_ps(h);
    __m256 indices = _mm256_set_ps(7, 6, 5, 4, 3, 2, 1, 0);

    for (; i <= n - 8; i += 8) {
        __m256 x_base = _mm256_set1_ps(a + i * h);
        __m256 x_vec = _mm256_add_ps(_mm256_mul_ps(indices, h_vec), x_base);

        __m256 f_vec = f_simd(x_vec);

        sum_vec = _mm256_add_ps(sum_vec, f_vec);
    }

    float sum = 0.0f;
    for (; i < n; ++i) {
        float x = a + i * h;
        sum += f(x);
    }

    alignas(32) float temp[8];
    _mm256_storeu_ps(temp, sum_vec);
    for (int j = 0; j < 8; ++j) {
        sum += temp[j];
    }

    return sum * h;
}

float integrate_simd_trapezoids_float(__m256 (*f_simd)(__m256), float (*f)(float), float a, float b, int n) {
    float h = (b - a) / n;
    __m256 sum_vec = _mm256_setzero_ps();
    int i = 1;

    float sum = 0.5f * (f(a) + f(b));

    __m256 h_vec = _mm256_set1_ps(h);
    __m256 indices = _mm256_set_ps(7, 6, 5, 4, 3, 2, 1, 0);

    for (; i <= n - 8; i += 8) {
        __m256 x_base = _mm256_set1_ps(a + i * h);
        __m256 x_vec = _mm256_add_ps(_mm256_mul_ps(indices, h_vec), x_base);

        __m256 f_vec = f_simd(x_vec);

        sum_vec = _mm256_add_ps(sum_vec, f_vec);
    }

    for (; i < n; ++i) {
        float x = a + i * h;
        sum += f(x);
    }

    alignas(32) float temp[8];
    _mm256_storeu_ps(temp, sum_vec);
    for (int j = 0; j < 8; ++j) {
        sum += temp[j];
    }

    return sum * h;
}

float integrate_simd_simpson_float(__m256 (*f_simd)(__m256), float (*f)(float), float a, float b, int n) {
    if (n % 2 != 0) {
        throw std::invalid_argument("Number of intervals must be even for Simpson's rule");
    }
    float h = (b - a) / n;
    __m256 sum_vec = _mm256_setzero_ps();
    int i = 1;

    float sum = f(a) + f(b);

    __m256 h_vec = _mm256_set1_ps(h);
    __m256 indices = _mm256_set_ps(7, 6, 5, 4, 3, 2, 1, 0);

    for (; i <= n - 8; i += 8) {
        __m256 x_base = _mm256_set1_ps(a + i * h);
        __m256 x_vec = _mm256_add_ps(_mm256_mul_ps(indices, h_vec), x_base);

        __m256 f_vec = f_simd(x_vec);

        __m256 coeff_vec = _mm256_set_ps(
            (i + 7) % 2 == 0 ? 2.0f : 4.0f,
            (i + 6) % 2 == 0 ? 2.0f : 4.0f,
            (i + 5) % 2 == 0 ? 2.0f : 4.0f,
            (i + 4) % 2 == 0 ? 2.0f : 4.0f,
            (i + 3) % 2 == 0 ? 2.0f : 4.0f,
            (i + 2) % 2 == 0 ? 2.0f : 4.0f,
            (i + 1) % 2 == 0 ? 2.0f : 4.0f,
            i % 2 == 0 ? 2.0f : 4.0f
        );

        f_vec = _mm256_mul_ps(f_vec, coeff_vec);

        sum_vec = _mm256_add_ps(sum_vec, f_vec);
    }

    for (; i < n; ++i) {
        float x = a + i * h;
        sum += (i % 2 == 0) ? 2 * f(x) : 4 * f(x);
    }

    alignas(32) float temp[8];
    _mm256_storeu_ps(temp, sum_vec);
    for (int j = 0; j < 8; ++j) {
        sum += temp[j];
    }

    return sum * h / 3.0f;
}