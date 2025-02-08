#include "math/pi_integrand.hpp"

double pi_integrand_double(double x) {
    return 4.0 / (1.0 + x * x);
}

float pi_integrand_float(float x) {
    return 4.0f / (1.0f + x * x);
}