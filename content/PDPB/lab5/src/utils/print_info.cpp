#include "utils/print_info.hpp"

#include <iomanip>
#include <iostream>

void printIntegrationBlockHeader() {
    std::cout << std::left << std::setw(INTEGRATION_FORMAT_METHOD_WIDTH) << "Method"
              << " " << std::setw(INTEGRATION_FORMAT_VALUE_WIDTH) << "Value"
              << " " << std::setw(INTEGRATION_FORMAT_TIME_WIDTH) << "Time (ms)" << std::endl
              << std::endl;
}

void printIntegrationBlockResult(const std::string& methodName, const double value, const double time) {
    std::cout << std::left << std::setw(INTEGRATION_FORMAT_METHOD_WIDTH) << methodName
              << " " << std::setw(INTEGRATION_FORMAT_VALUE_WIDTH) << std::fixed
              << std::setprecision(INTEGRATION_FORMAT_PI_PRECISION) << value << " "
              << std::setw(INTEGRATION_FORMAT_TIME_WIDTH) << std::fixed
              << std::setprecision(INTEGRATION_FORMAT_TIME_PRECISION) << time << std::endl;
}