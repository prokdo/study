#pragma once

#include <immintrin.h>

__m256d pi_integrand_simd_double(__m256d vec_x);

__m256 pi_integrand_simd_float(__m256 vec_x);