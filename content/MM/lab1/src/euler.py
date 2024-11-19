from typing import Callable, Tuple

import numpy as np

# Явный метод Эйлера
def euler_explicit(
                    f: Callable[[float, float], float],
                    t0: float,
                    y0: float,
                    t_end: float,
                    step: float) -> Tuple[np.ndarray, np.ndarray]:
    t_values = np.arange(t0, t_end + step, step)
    y_values = np.zeros_like(t_values)
    y_values[0] = y0
    for i in range(1, len(t_values)):
        y_values[i] = y_values[i - 1] + step * f(t_values[i - 1], y_values[i - 1])
    return t_values, y_values

# Неявный метод Эйлера
def euler_implicit(
                    f: Callable[[float, float], float],
                    t0: float,
                    y0: float,
                    t_end: float,
                    step: float) -> Tuple[np.ndarray, np.ndarray]:
    t_values = np.arange(t0, t_end + step, step)
    y_values = np.zeros_like(t_values)
    y_values[0] = y0
    for i in range(1, len(t_values)):
        # Решаем y_n+1 = y_n + h * f(t_n+1, y_n+1)
        y_prev = y_values[i - 1]
        t_next = t_values[i]
        y_next = y_prev
        for _ in range(10):
            y_next = y_prev + step * f(t_next, y_next)
        y_values[i] = y_next
    return t_values, y_values

# Весовая схема
def euler_weighted(
                    f: Callable[[float, float], float],
                    t0: float,
                    y0: float,
                    t_end: float,
                    step: float,
                    weight: float = 0.5) -> Tuple[np.ndarray, np.ndarray]:
    t_values = np.arange(t0, t_end + step, step)
    y_values = np.zeros_like(t_values)
    y_values[0] = y0
    for i in range(1, len(t_values)):
        y_prev = y_values[i - 1]
        t_curr = t_values[i - 1]
        t_next = t_values[i]
        # Решаем y_n+1 = y_n + h * (theta * f(t_n+1, y_n+1) + (1 - theta) * f(t_n, y_n))
        y_next = y_prev
        for _ in range(10):
            y_next = y_prev + step * (
                weight * f(t_next, y_next) + (1 - weight) * f(t_curr, y_prev)
            )
        y_values[i] = y_next
    return t_values, y_values