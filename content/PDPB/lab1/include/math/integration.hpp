#pragma once

double integrate_rectangles_double(double (*f)(double), double a, double b, int n);
double integrate_trapezoids_double(double (*f)(double), double a, double b, int n);
double integrate_simpson_double(double (*f)(double), double a, double b, int n);

float integrate_rectangles_float(float (*f)(float), float a, float b, int n);
float integrate_trapezoids_float(float (*f)(float), float a, float b, int n);
float integrate_simpson_float(float (*f)(float), float a, float b, int n);