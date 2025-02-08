#include <iostream>
#include <iomanip>

#include "math/integration.hpp"
#include "math/simd_integration.hpp"
#include "math/pi_integrand.hpp"
#include "math/pi_integrand_simd.hpp"
#include "utils/timer.hpp"

const int FORMAT_PI_PRECISION = 12;
const int FORMAT_ETA_PRECISION = 3;

const int FORMAT_METHOD_WIDTH = 30;
const int FORMAT_VALUE_WIDTH = FORMAT_PI_PRECISION + 5;
const int FORMAT_ETA_WIDTH = FORMAT_ETA_PRECISION + 5;

void printHeader() {
    std::cout << std::left << std::setw(FORMAT_METHOD_WIDTH) << "Method"
              << " " << std::setw(FORMAT_VALUE_WIDTH) << "Value"
              << " " << std::setw(FORMAT_ETA_WIDTH) << "ETA (ms)" << std::endl
              << std::endl;
}

void printResult(const std::string& methodName, double value, double eta) {
    std::cout << std::left << std::setw(FORMAT_METHOD_WIDTH) << methodName
              << " " << std::setw(FORMAT_VALUE_WIDTH) << std::fixed
              << std::setprecision(FORMAT_PI_PRECISION) << value << " "
              << std::setw(FORMAT_ETA_WIDTH) << std::fixed
              << std::setprecision(FORMAT_ETA_PRECISION) << eta << std::endl;
}

int main() {
    const double a_double = 0.0, b_double = 1.0;
    const float a_float = 0.0f, b_float = 1.0f;
    const int n = 8000000;

    Timer timer;

    printHeader();

    {
        timer.start();
        double pi_rectangles = integrate_rectangles_double(pi_integrand_double, a_double, b_double, n);
        timer.stop();
        printResult("Rectangles (double)", pi_rectangles, timer.elapsedMilliseconds());

        timer.start();
        double pi_trapezoids = integrate_trapezoids_double(pi_integrand_double, a_double, b_double, n);
        timer.stop();
        printResult("Trapezoids (double)", pi_trapezoids, timer.elapsedMilliseconds());

        timer.start();
        double pi_simpson = integrate_simpson_double(pi_integrand_double, a_double, b_double, n);
        timer.stop();
        printResult("Simpson (double)", pi_simpson, timer.elapsedMilliseconds());
    }

    std::cout << std::endl;

    {
        timer.start();
        double pi_rectangles_simd = integrate_simd_rectangles_double(pi_integrand_simd_double, pi_integrand_double, a_double, b_double, n);
        timer.stop();
        printResult("Rectangles (SIMD, double)", pi_rectangles_simd, timer.elapsedMilliseconds());

        timer.start();
        double pi_trapezoids_simd = integrate_simd_trapezoids_double(pi_integrand_simd_double, pi_integrand_double, a_double, b_double, n);
        timer.stop();
        printResult("Trapezoids (SIMD, double)", pi_trapezoids_simd, timer.elapsedMilliseconds());

        timer.start();
        double pi_simpson_simd = integrate_simd_simpson_double(pi_integrand_simd_double, pi_integrand_double, a_double, b_double, n);
        timer.stop();
        printResult("Simpson (SIMD, double)", pi_simpson_simd, timer.elapsedMilliseconds());
    }

    std::cout << std::endl;

    {
        timer.start();
        float pi_rectangles = integrate_rectangles_float(pi_integrand_float, a_float, b_float, n);
        timer.stop();
        printResult("Rectangles (float)", pi_rectangles, timer.elapsedMilliseconds());

        timer.start();
        float pi_trapezoids = integrate_trapezoids_float(pi_integrand_float, a_float, b_float, n);
        timer.stop();
        printResult("Trapezoids (float)", pi_trapezoids, timer.elapsedMilliseconds());

        timer.start();
        float pi_simpson = integrate_simpson_float(pi_integrand_float, a_float, b_float, n);
        timer.stop();
        printResult("Simpson (float)", pi_simpson, timer.elapsedMilliseconds());
    }

    std::cout << std::endl;

    {
        timer.start();
        float pi_rectangles_simd = integrate_simd_rectangles_float(pi_integrand_simd_float, pi_integrand_float, a_float, b_float, n);
        timer.stop();
        printResult("Rectangles (SIMD, float)", pi_rectangles_simd, timer.elapsedMilliseconds());

        timer.start();
        float pi_trapezoids_simd = integrate_simd_trapezoids_float(pi_integrand_simd_float, pi_integrand_float, a_float, b_float, n);
        timer.stop();
        printResult("Trapezoids (SIMD, float)", pi_trapezoids_simd, timer.elapsedMilliseconds());

        timer.start();
        float pi_simpson_simd = integrate_simd_simpson_float(pi_integrand_simd_float, pi_integrand_float, a_float, b_float, n);
        timer.stop();
        printResult("Simpson (SIMD, float)", pi_simpson_simd, timer.elapsedMilliseconds());
    }

    std::cout << std::endl;

    return 0;
}