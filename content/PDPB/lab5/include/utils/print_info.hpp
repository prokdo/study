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