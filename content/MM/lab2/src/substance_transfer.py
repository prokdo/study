from typing import Callable

import numpy as np

# Схема "против потока"
def upwind_scheme(
                    c: np.ndarray,
                    v: float,
                    D: float,
                    dx: float,
                    dt: float) -> np.ndarray:
    c_new = c.copy()
    for i in range(1, len(c) - 1):
        adv = -v * (c[i] - c[i - 1]) / dx
        diff = D * (c[i + 1] - 2 * c[i] + c[i - 1]) / dx**2
        c_new[i] = c[i] + dt * (adv + diff)
    return c_new

# Центральная разностная схема
def central_difference_scheme(
                    c: np.ndarray,
                    v: float,
                    D: float,
                    dx: float,
                    dt: float) -> np.ndarray:
    c_new = c.copy()
    for i in range(1, len(c) - 1):
        adv = -v * (c[i + 1] - c[i - 1]) / (2 * dx)
        diff = D * (c[i + 1] - 2 * c[i] + c[i - 1]) / dx**2
        c_new[i] = c[i] + dt * (adv + diff)
    return c_new

# Схема "кабаре"
def cabare_scheme(
                    c: np.ndarray,
                    v: float,
                    D: float,
                    dx: float,
                    dt: float) -> np.ndarray:
    c_new = c.copy()
    for i in range(1, len(c) - 1):
        if v < 0:
            adv_flux = -v * (c[i + 1] - c[i]) / dx
        else:
            adv_flux = -v * (c[i] - c[i - 1]) / dx
        diff_flux = D * (c[i + 1] - 2 * c[i] + c[i - 1]) / dx**2
        c_new[i] = c[i] + dt * (adv_flux + diff_flux)
    return c_new

# Схема "крест"
def cross_scheme(
                    c: np.ndarray,
                    v: float,
                    D: float,
                    dx: float,
                    dt: float) -> np.ndarray:
    c_new = c.copy()
    for i in range(1, len(c) - 1):
        adv = -v * (c[i + 1] - c[i - 1]) / (2 * dx)
        diff = D * (c[i + 1] - 2 * c[i] + c[i - 1]) / dx**2
        c_new[i] = c[i] + dt * (adv + diff)
    return c_new

# Схема "правый уголок"
def right_corner_scheme(
                    c: np.ndarray,
                    v: float,
                    D: float,
                    dx: float,
                    dt: float) -> np.ndarray:
    c_new = c.copy()
    for i in range(1, len(c) - 1):
        adv = -v * (c[i + 1] - c[i]) / dx
        diff = D * (c[i + 1] - 2 * c[i] + c[i - 1]) / dx**2
        c_new[i] = c[i] + dt * (adv + diff)
    return c_new

# Линейная комбинация двух схем
def linear_combination_scheme(
                    first_scheme: Callable[[np.ndarray, float, float, float, float], np.ndarray],
                    second_scheme: Callable[[np.ndarray, float, float, float, float], np.ndarray],
                    c: np.ndarray,
                    v: float,
                    D: float,
                    dx: float,
                    dt: float,
                    alpha: float = 0.5) -> np.ndarray:
    c_first = first_scheme(c, v, D, dx, dt)
    c_second = second_scheme(c, v, D, dx, dt)
    c_new = alpha * c_first + (1 - alpha) * c_second
    return c_new

def solve(
                    scheme: Callable[[np.ndarray, float, float, float, float], np.ndarray],
                    Nt: int,
                    c: np.ndarray,
                    v: float,
                    D: float,
                    dx: float,
                    dt: float) -> np.ndarray:
    c_new: np.ndarray = c.copy()
    for _ in range(Nt):
        c_new = scheme(c_new, v, D, dx, dt)
    total_mass = np.sum(c)
    c_new *= total_mass / np.sum(c_new)
    return c_new

def linear_combination_solve(
                    first_scheme: Callable[[np.ndarray, float, float, float, float], np.ndarray],
                    second_scheme: Callable[[np.ndarray, float, float, float, float], np.ndarray],
                    Nt: int,
                    c: np.ndarray,
                    v: float,
                    D: float,
                    dx: float,
                    dt: float,
                    alpha: float = 0.5) -> np.ndarray:
    c_new: np.ndarray = c.copy()
    for _ in range(Nt):
        c_new = linear_combination_scheme(first_scheme, second_scheme, c_new, v, D, dx, dt, alpha)
    total_mass = np.sum(c)
    c_new *= total_mass / np.sum(c_new)
    return c_new
