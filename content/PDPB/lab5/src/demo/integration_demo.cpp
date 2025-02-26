#include "demo/integration_demo.hpp"

#include <iostream>
#include <mpi.h>

#include "math/integration.hpp"
#include "utils/print_info.hpp"
#include "utils/timer.hpp"

void integration_demo(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int rank;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    if (rank == 0) {
        std::cout << "N = " << N << std::endl << std::endl;

        printIntegrationBlockHeader();

        Timer timer;
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
    }

    MPI_Barrier(MPI_COMM_WORLD);
    {
        Timer timer;
        double result = 0.0;

        timer.start();
        result = integrate_rectangles_mpi_double(pi_integrand_double, A_DOUBLE, B_DOUBLE, N);
        MPI_Barrier(MPI_COMM_WORLD);
        if (rank == 0) {
            timer.stop();
            printIntegrationBlockResult("Rectangles (MPI, double)", result, timer.elapsedMilliseconds());
        }

        timer.start();
        result = integrate_trapezoids_mpi_double(pi_integrand_double, A_DOUBLE, B_DOUBLE, N);
        MPI_Barrier(MPI_COMM_WORLD);
        if (rank == 0) {
            timer.stop();
            printIntegrationBlockResult("Trapezoids (MPI, double)", result, timer.elapsedMilliseconds());
        }

        timer.start();
        result = integrate_simpson_mpi_double(pi_integrand_double, A_DOUBLE, B_DOUBLE, N);
        MPI_Barrier(MPI_COMM_WORLD);
        if (rank == 0) {
            timer.stop();
            printIntegrationBlockResult("Simpson (MPI, double)", result, timer.elapsedMilliseconds());
        }
    }
    if (rank == 0) std::cout << std::endl;

    if (rank == 0) {
        Timer timer;
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
    }

    MPI_Barrier(MPI_COMM_WORLD);
    {
        Timer timer;
        float result = 0.0f;

        timer.start();
        result = integrate_rectangles_mpi_float(pi_integrand_float, A_FLOAT, B_FLOAT, N);
        MPI_Barrier(MPI_COMM_WORLD);
        if (rank == 0) {
            timer.stop();
            printIntegrationBlockResult("Rectangles (MPI, float)", result, timer.elapsedMilliseconds());
        }

        timer.start();
        result = integrate_trapezoids_mpi_float(pi_integrand_float, A_FLOAT, B_FLOAT, N);
        MPI_Barrier(MPI_COMM_WORLD);
        if (rank == 0) {
            timer.stop();
            printIntegrationBlockResult("Trapezoids (MPI, float)", result, timer.elapsedMilliseconds());
        }

        timer.start();
        result = integrate_simpson_mpi_float(pi_integrand_float, A_FLOAT, B_FLOAT, N);
        MPI_Barrier(MPI_COMM_WORLD);
        if (rank == 0) {
            timer.stop();
            printIntegrationBlockResult("Simpson (MPI, float)", result, timer.elapsedMilliseconds());
        }
    }

    MPI_Finalize();
}