#pragma once

#include <cstdint>
#include <string>

const uint8_t INTEGRATION_FORMAT_PI_PRECISION   = 12;
const uint8_t INTEGRATION_FORMAT_TIME_PRECISION = 3;
const uint8_t INTEGRATION_FORMAT_METHOD_WIDTH   = 30;
const uint8_t INTEGRATION_FORMAT_VALUE_WIDTH    = INTEGRATION_FORMAT_PI_PRECISION + 5;
const uint8_t INTEGRATION_FORMAT_TIME_WIDTH     = INTEGRATION_FORMAT_TIME_PRECISION + 5;

void printIntegrationBlockHeader();
void printIntegrationBlockResult(const std::string& methodName, const double value, const double time);

const uint8_t SORT_FORMAT_TIME_PRECISION        = 3;
const uint8_t SORT_FORMAT_METHOD_WIDTH          = 30;
const uint8_t SORT_FORMAT_VECTOR_SIZE_WIDTH     = 20;
const uint8_t SORT_FORMAT_TIME_WIDTH            = SORT_FORMAT_TIME_PRECISION + 5;

void printSortBlockHeader();
void printSortBlockResult(const std::string& methodName, const size_t vectorSize, const double time);

const uint8_t MATRICES_FORMAT_TIME_PRECISION    = 3;
const uint8_t MATRICES_FORMAT_METHOD_WIDTH      = 30;
const uint8_t MATRICES_FORMAT_MATRIX_SIZE_WIDTH = 20;
const uint8_t MATRICES_FORMAT_TIME_WIDTH        = MATRICES_FORMAT_TIME_PRECISION + 5;

void printMatricesBlockHeader();
void printMatricesBlockResult(const std::string& methodName, const std::string& matrixSize, const double time);