#include "demo/integration_demo.hpp"

#include <iostream>

#include "math/integration.hpp"
#include "utils/print_info.hpp"
#include "utils/timer.hpp"

void integration_demo() {
    std::cout << "--- Integration ---" << std::endl << std::endl;

    std::cout << "N = " << N << std::endl << std::endl;

    Timer timer;

    printIntegrationBlockHeader();

    {
        timer.start();
        double pi_rectangles = integrate_rectangles_double(pi_integrand_double, A_DOUBLE, B_DOUBLE, N);
        timer.stop();
        printIntegrationBlockResult("Rectangles (double)", pi_rectangles, timer.elapsedMilliseconds());

        timer.start();
        double pi_trapezoids = integrate_trapezoids_double(pi_integrand_double, A_DOUBLE, B_DOUBLE, N);
        timer.stop();
        printIntegrationBlockResult("Trapezoids (double)", pi_trapezoids, timer.elapsedMilliseconds());

        timer.start();
        double pi_simpson = integrate_simpson_double(pi_integrand_double, A_DOUBLE, B_DOUBLE, N);
        timer.stop();
        printIntegrationBlockResult("Simpson (double)", pi_simpson, timer.elapsedMilliseconds());
    }

    std::cout << std::endl;

    {
        timer.start();
        double pi_rectangles_omp = integrate_rectangles_omp_double(pi_integrand_double, A_DOUBLE, B_DOUBLE, N);
        timer.stop();
        printIntegrationBlockResult("Rectangles (OpenMP, double)", pi_rectangles_omp, timer.elapsedMilliseconds());

        timer.start();
        double pi_trapezoids_omp = integrate_rectangles_omp_double(pi_integrand_double, A_DOUBLE, B_DOUBLE, N);
        timer.stop();
        printIntegrationBlockResult("Trapezoids (OpenMP, double)", pi_trapezoids_omp, timer.elapsedMilliseconds());

        timer.start();
        double pi_simpson_omp = integrate_simpson_omp_double(pi_integrand_double, A_DOUBLE, B_DOUBLE, N);
        timer.stop();
        printIntegrationBlockResult("Simpson (OpenMP, double)", pi_simpson_omp, timer.elapsedMilliseconds());
    }

    std::cout << std::endl;

    {
        timer.start();
        float pi_rectangles = integrate_rectangles_float(pi_integrand_float, A_FLOAT, B_FLOAT, N);
        timer.stop();
        printIntegrationBlockResult("Rectangles (float)", pi_rectangles, timer.elapsedMilliseconds());

        timer.start();
        float pi_trapezoids = integrate_trapezoids_float(pi_integrand_float, A_FLOAT, B_FLOAT, N);
        timer.stop();
        printIntegrationBlockResult("Trapezoids (float)", pi_trapezoids, timer.elapsedMilliseconds());

        timer.start();
        float pi_simpson = integrate_simpson_float(pi_integrand_float, A_FLOAT, B_FLOAT, N);
        timer.stop();
        printIntegrationBlockResult("Simpson (float)", pi_simpson, timer.elapsedMilliseconds());
    }

    std::cout << std::endl;

    {
        timer.start();
        float pi_rectangles_omp = integrate_rectangles_omp_float(pi_integrand_float, A_FLOAT, B_FLOAT, N);
        timer.stop();
        printIntegrationBlockResult("Rectangles (OpenMP, float)", pi_rectangles_omp, timer.elapsedMilliseconds());

        timer.start();
        float pi_trapezoids_omp = integrate_trapezoids_omp_float(pi_integrand_float, A_FLOAT, B_FLOAT, N);
        timer.stop();
        printIntegrationBlockResult("Trapezoids (OpenMP, float)", pi_trapezoids_omp, timer.elapsedMilliseconds());

        timer.start();
        float pi_simpson_omp = integrate_simpson_omp_float(pi_integrand_float, A_FLOAT, B_FLOAT, N);
        timer.stop();
        printIntegrationBlockResult("Simpson (OpenMP, float)", pi_simpson_omp, timer.elapsedMilliseconds());
    }

    std::cout << std::endl;
}