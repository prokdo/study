#pragma once

#include <immintrin.h>

double integrate_simd_rectangles_double(__m256d (*f_simd)(__m256d), double (*f)(double), double a, double b, int n);
double integrate_simd_trapezoids_double(__m256d (*f_simd)(__m256d), double (*f)(double), double a, double b, int n);
double integrate_simd_simpson_double(__m256d (*f_simd)(__m256d), double (*f)(double), double a, double b, int n);

float integrate_simd_rectangles_float(__m256 (*f_simd)(__m256), float (*f)(float), float a, float b, int n);
float integrate_simd_trapezoids_float(__m256 (*f_simd)(__m256), float (*f)(float), float a, float b, int n);
float integrate_simd_simpson_float(__m256 (*f_simd)(__m256), float (*f)(float), float a, float b, int n);