#include "math/pi_integrand_simd.hpp"

__m256d pi_integrand_simd_double(__m256d x_vec) {
    __m256d one = _mm256_set1_pd(1.0);
    __m256d four = _mm256_set1_pd(4.0);

    __m256d denominator = _mm256_add_pd(one, _mm256_mul_pd(x_vec, x_vec));

    return _mm256_div_pd(four, denominator);
}

__m256 pi_integrand_simd_float(__m256 x_vec) {
    __m256 one = _mm256_set1_ps(1.0f);
    __m256 four = _mm256_set1_ps(4.0f);

    __m256 denominator = _mm256_add_ps(one, _mm256_mul_ps(x_vec, x_vec));

    return _mm256_div_ps(four, denominator);
}