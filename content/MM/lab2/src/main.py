import numpy as np
import matplotlib.pyplot as plt

from substance_transfer import upwind_scheme, central_difference_scheme, cabare_scheme, cross_scheme, right_corner_scheme, solve, linear_combination_solve

if __name__ == '__main__':
    # Параметры задачи
    L: float = 100.0            # Длина области
    T: float = 100.0            # Время моделирования
    Nx: int = 100               # Количество узлов по пространству
    Nt: int = 1000              # Количество шагов по времени
    v: float = 0.5              # Скорость переноса
    D: float = 1.0              # Коэффициент диффузии
    dx: float = L / (Nx - 1)    # Шаг по пространству
    dt: float = T / Nt          # Шаг по времени

    # Проверка на устойчивость
    assert v * dt / dx <= 1, "Условие Куранта нарушено"
    assert v * dt / dx <= 10.0, "Число Перкле слишком велико"
    assert D * dt / dx**2 <= 0.5, "Число Фурье слишком велико"


    # Инициализация сетки
    x: np.ndarray = np.linspace(0, L, Nx)

    # Начальное условие: концентрация вещества сосредоточена в центре
    c: np.ndarray = np.zeros(Nx)
    c[Nx // 2] = 1

    # Получение решений для всех схем
    c_upwind: np.ndarray = solve(upwind_scheme, Nt, c, v, D, dx, dt)
    c_central: np.ndarray = solve(central_difference_scheme, Nt, c, v, D, dx, dt)
    c_cabare: np.ndarray = solve(cabare_scheme, Nt, c, v, D, dx, dt)
    c_cross: np.ndarray = solve(cross_scheme, Nt, c, v, D, dx, dt)
    c_right: np.ndarray = solve(right_corner_scheme, Nt, c, v, D, dx, dt)
    c_cabare_central_linear_comb: np.ndarray = linear_combination_solve(cabare_scheme, central_difference_scheme, Nt, c, v, D, dx, dt)
    c_cabare_cross_linear_comb: np.ndarray = linear_combination_solve(cabare_scheme, cross_scheme, Nt, c, v, D, dx, dt)

    # Построение графиков
    plt.figure(figsize=(12, 8), num="Лабораторная работа №2. Задача переноса веществ")
    plt.plot(x, c_upwind, label='Схема «против потока»', linestyle='--')
    plt.plot(x, c_central, label="Центральная разностная схема", linestyle='-.')
    plt.plot(x, c_cabare, label='Схема «кабаре»')
    plt.plot(x, c_cross, label='Схема «крест»', linestyle='-')
    plt.plot(x, c_right, label='Схема «правого уголка»', linestyle='--')
    plt.plot(x, c_cabare_central_linear_comb, label='Линейная комбинация «кабаре» и центральной разностной схем', linewidth=2, linestyle=(0, (1, 1)))
    plt.plot(x, c_cabare_cross_linear_comb, label='Линейная комбинация схем «кабаре» и «крест»', linestyle='-.', linewidth=2)
    plt.xlabel("x")
    plt.ylabel("c(x)")
    plt.title("Численные решения задачи переноса веществ")
    plt.grid()
    plt.legend()
    plt.show()
