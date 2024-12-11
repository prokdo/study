import matplotlib.pyplot as plt
import numpy as np

from substance_transfer import right_corner_scheme, central_difference_scheme, cabare_scheme, linear_combination_scheme
# from substance_transfer import cross_scheme

if __name__ == '__main__':
    # Параметры задачи
    L:  float   = 160.0
    T:  int     = 100
    v:  float   = -0.5
    dx: float   = 1.0
    dt: float   = 0.1
    Nx: int     = int(L / dx)
    Nt: int     = int(T / dt)

    # Проверка условия Куранта
    assert abs(v) * dt / dx <= 1, 'Условие Куранта не выполняется'

    # Функция начального распределения концентрации
    def f(x: np.ndarray) -> np.ndarray:
        return np.maximum(-1 / 100 * (x - 80) ** 2 + 1, 0)

    # Инициализация сетки
    x = np.arange(0, L, dx, dtype=float)

    # Вычисление значений для различных схем
    c_init                      = f(x)
    c_upwind                    = right_corner_scheme(Nt, c_init, v, dt, dx)
    c_central                   = central_difference_scheme(Nt, c_init, v, dt, dx)
    c_cabare                    = cabare_scheme(Nt, c_init, v, dt, dx)
    # c_cross                     = cross_scheme(Nt, c_init, v, dt, dx)
    c_central_cabare_combination = linear_combination_scheme(central_difference_scheme, cabare_scheme, Nt, c_init, v, dt, dx)
    # c_cross_cabare_combination = linear_combination_scheme(cross_scheme, cabare_scheme, Nt, c_init, v, dt, dx)

    # Построение графиков
    plt.figure(figsize=(12, 8), num='Лабораторная работа №2. Задача переноса веществ')
    plt.plot(x, c_init,                         label='Исходная функция параболы',      color='black')
    plt.plot(x, c_upwind,                       label='Схема «правый уголок»',          linestyle='--')
    plt.plot(x, c_central,                      label='Центральная разностная схема',   linestyle='-.')
    plt.plot(x, c_cabare,                       label='Схема «кабаре»',                 linestyle=':')
    # plt.plot(x, c_cross,                        label='Схема «крест»',                  linestyle='--')
    plt.plot(x, c_central_cabare_combination,   label='Линейная комбинация центральной разностной и «кабаре» схем')
    # plt.plot(x, c_cross_cabare_combination,    label='Линейная комбинация схем «крест» и «кабаре»', linestyle='-.')

    plt.title('Численные решения задачи переноса веществ')
    plt.xlabel('x')
    plt.ylabel('c(x)')
    plt.legend()
    plt.grid()
    plt.show()
