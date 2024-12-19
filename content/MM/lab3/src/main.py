from typing import List

import numpy as np
import matplotlib.pyplot as plt

from thermal_conduction import analytic_method, explicit_method, implicit_method

if __name__ == "__main__":
    # Параметры задачи
    N:                  int         = 100
    L:                  int         = 120
    dx:                 float       = 1.0
    dt:                 float       = 0.1
    alpha:              float       = 1.0
    simulation_periods: List[int]   = [10, 25, 50, 75, 100]

    # Функция начального распределения теплоты
    def f(x: np.ndarray) -> np.ndarray:
        result = np.zeros_like(x)
        result = np.where((x >= 50) & (x <= 60), 0.1 * x - 5, result)
        result = np.where((x > 60) & (x <= 70), -0.01 * (x - 60) ** 2 + 1, result)
        return result

    # Формула расчета коэффициента ряда Фурье
    def b_n(L: int, n: int) -> float:
        first_integral: float = ((1000 * np.sin(np.pi * n * 3 / 5) - 100 * np.pi * n * np.cos(np.pi * 3 * n / 5) - 1000 * np.sin(np.pi * n / 2)) / (np.pi ** 2 * n ** 2))

        second_integral: float = ((2000 * np.pi * n * np.sin(np.pi * n * 7 / 10) + 20000 * np.cos(np.pi * n * 7 / 10) + (-100 * np.pi ** 2 * n ** 2 - 20000) * np.cos(3 * np.pi * n / 5)) / - (np.pi ** 3 * n ** 3))

        return (2 / L) * (first_integral + second_integral)

    # Расчет начального распределения теплоты
    x: np.ndarray = np.arange(0, L + 1, dx)
    u_init: np.ndarray = f(x)

    figure = plt.figure(figsize=(15, 12), num='Лабораторная работа №3. Задача теплопроводности')
    gridspec = figure.add_gridspec(2, 2, height_ratios=[1, 1])

    # Аналитического решение
    graph_analytic = figure.add_subplot(gridspec[:, 0])
    graph_analytic.plot(x, u_init, label='Начальное распределение теплоты', color='black')
    u_analytic: List[np.ndarray] = [analytic_method(b_n, N, x - dx / 2, L, period, alpha) for period in simulation_periods]
    for period, result in enumerate(u_analytic):
        graph_analytic.plot(x, result, label=f't = {simulation_periods[period]}')
    graph_analytic.set_title('Аналитическое решение')
    graph_analytic.set_xlabel('x')
    graph_analytic.set_ylabel('u(x)')
    graph_analytic.grid()
    graph_analytic.legend()

    # Явный метод
    graph_explicit = figure.add_subplot(gridspec[0, 1])
    graph_explicit.plot(x, u_init, label='Начальное распределение теплоты', color='black')
    u_explicit: List[np.ndarray] = [explicit_method(u_init, L, period, dx, dt, alpha) for period in simulation_periods]
    for period, result in enumerate(u_explicit):
        graph_explicit.plot(x, result, label=f't = {simulation_periods[period]}')
    graph_explicit.set_title('Явный метод')
    graph_explicit.set_xlabel('x')
    graph_explicit.set_ylabel('u(x)')
    graph_explicit.grid()
    graph_explicit.legend()

    # Неявный метод
    graph_implicit = figure.add_subplot(gridspec[1, 1])
    graph_implicit.plot(x, u_init, label='Начальное распределение теплоты', color='black')
    u_implicit: List[np.ndarray] = [implicit_method(u_init, L, period, dx, dt, alpha) for period in simulation_periods]
    for period, result in enumerate(u_implicit):
        graph_implicit.plot(x, result, label=f't = {simulation_periods[period]}')
    graph_implicit.set_title('Неявный метод')
    graph_implicit.set_xlabel('x')
    graph_implicit.set_ylabel('u(x)')
    graph_implicit.grid()
    graph_implicit.legend()

    plt.tight_layout()
    plt.show()
