import numpy as np
import matplotlib.pyplot as plt

from euler import euler_explicit, euler_implicit, euler_weighted

if __name__ == '__main__':
    # Начальные условия
    def f(t:float, y: float) -> float:
        return (-y**2) / (4 + t**2)

    def F(t: np.ndarray) -> np.ndarray:
        return 2 / (np.arctan(t / 2) + 2)

    t0, y0 = 0, 1
    t_end = 10
    step = 0.1

    # Аналитическое решение
    t_exact = np.linspace(t0, t_end, 1000)
    y_exact = F(t_exact)

    # Решение методом Эйлера
    t_exp, y_exp = euler_explicit(f, t0, y0, t_end, step)
    t_imp, y_imp = euler_implicit(f, t0, y0, t_end, step)
    t_w, y_w = euler_weighted(f, t0, y0, t_end, step, weight=0.5)

    # Вычисление погрешностей
    error_exp = np.abs(F(t_exp) - y_exp)
    error_imp = np.abs(F(t_imp) - y_imp)
    error_w = np.abs(F(t_w) - y_w)

    # Создание холста с двумя графиками параллельно
    fig, axes = plt.subplots(1, 2, figsize=(14, 7))
    fig.canvas.manager.set_window_title("Лабораторная работа №1. Задача Коши")

    # Левый график — решения
    axes[0].plot(t_exp, y_exp, label="Явная схема Эйлера")
    axes[0].plot(t_imp, y_imp, label="Неявная схема Эйлера")
    axes[0].plot(t_w, y_w, label="Весовая схема Эйлера")
    axes[0].plot(t_exact, y_exact, label="Аналитическое решение", linestyle="dashed")
    axes[0].set_xlabel("t")
    axes[0].set_ylabel("y")
    axes[0].set_title("Решения методом Эйлера")
    axes[0].legend()
    axes[0].grid()

    # Правый график — ошибки
    axes[1].plot(t_exp, error_exp, label="Погрешность явной схемы")
    axes[1].plot(t_imp, error_imp, label="Погрешность неявной схемы")
    axes[1].plot(t_w, error_w, label="Погрешность весовой схемы")
    axes[1].set_xlabel("t")
    axes[1].set_ylabel("Ошибка")
    axes[1].set_title("Погрешности метода Эйлера")
    axes[1].legend()
    axes[1].grid()

    # Автоматическая настройка отступов
    plt.tight_layout()
    plt.show()
