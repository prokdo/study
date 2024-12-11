from typing import Callable

import numpy as np

# Схема "правый уголок"
def right_corner_scheme(
                    Nt: int,
                    c_init: np.ndarray,
                    v: float,
                    dt: float,
                    dx: float) -> np.ndarray:
    c = np.zeros((Nt, c_init.size))
    c[0] = c_init
    for n in range(1, Nt):
        for i in range(c_init.size):
            c_n_i = c[n - 1, i]
            if v > 0:
                c_n_im1 = c[n - 1, i - 1] if i - 1 >= 0 else 0
                c[n, i] = c_n_i - v * dt * (c_n_i - c_n_im1) / dx
            else:
                c_n_ip1 = c[n - 1, i + 1] if i + 1 < c_init.size else 0
                c[n, i] = c_n_i - (-v) * dt * (c_n_i - c_n_ip1) / dx
    return c[-1] * np.sum(c_init) / np.sum(c[-1])

# Центральная разностная схема
def central_difference_scheme(
                    Nt: int,
                    c_init: np.ndarray,
                    v: float,
                    dt: float,
                    dx: float) -> np.ndarray:
    c = np.zeros((Nt, c_init.size))
    c[0] = c_init
    for n in range(1, Nt):
        c_n = c[n - 1]
        c_n_ip1 = np.roll(c_n, -1)
        c_n_im1 = np.roll(c_n, 1)
        c[n] = c_n - v * dt * (c_n_ip1 - c_n_im1) / (2 * dx)
    return c[-1] * np.sum(c_init) / np.sum(c[-1])

# Схема "кабаре"
def cabare_scheme(
                    Nt: int,
                    c_init: np.ndarray,
                    v: float,
                    dt: float,
                    dx: float) -> np.ndarray:
    c = np.zeros((Nt, c_init.size))
    c[0] = c_init
    for n in range(1, Nt):
        for i in range(c_init.size):
            c_n_i = c[n - 1, i]
            if v > 0:
                c_n_im1 = c[n - 1, i - 1] if i - 1 >= 0 else 0
                c_nm1_im1 = c[n - 2, i - 1] if (i - 1 >= 0 and n - 2 >= 0) else 0
                c[n, i] = c_n_i - (c_n_im1 - c_nm1_im1) - (2 * dt * v) / dx * (c_n_i - c_n_im1)
            else:
                c_n_ip1 = c[n - 1, i + 1] if i + 1 < c_init.size else 0
                c_nm1_ip1 = c[n - 2, i + 1] if (i + 1 < c_init.size and n - 2 >= 0) else 0
                c[n, i] = c_n_i - (c_n_ip1 - c_nm1_ip1) - (2 * dt * -v) / dx * (c_n_i - c_n_ip1)
    return c[-1] * np.sum(c_init) / np.sum(c[-1])

# Схема "крест"
def cross_scheme(
                    Nt: int,
                    c_init: np.ndarray,
                    v: float,
                    dt: float,
                    dx: float) -> np.ndarray:
    c = np.zeros((Nt, c_init.size))
    c[0] = c_init
    c[1] = c_init
    for n in range(1, Nt - 1):
        for i in range(1, c_init.size - 1):
            c[n + 1, i] = c[n - 1, i] - v * dt / dx * (c[n, i + 1] - c[n, i - 1])
    return c[-1] * np.sum(c_init) / np.sum(c[-1])

# Линейная комбинация двух численных схем
def linear_combination_scheme(
                    first_scheme: Callable[[int, np.ndarray, float, float, float], np.ndarray],
                    second_scheme: Callable[[int, np.ndarray, float, float, float], np.ndarray],
                    Nt: int,
                    c_init: np.ndarray,
                    v: float,
                    dt: float,
                    dx: float,
                    alpha: float = 0.5) -> np.ndarray:
    if not 0 <= alpha <= 1:
        raise ValueError("Linear combination coefficient must be in range [0; 1]")
    c_first = first_scheme(Nt, c_init, v, dt, dx)
    c_second = second_scheme(Nt, c_init, v, dt, dx)
    return alpha * c_first + (1 - alpha) * c_second
