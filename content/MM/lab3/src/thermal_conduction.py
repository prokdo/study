from typing import Callable

import numpy as np

def analytic_method(
                    b_n:    Callable[[int, int], float],
                    N:      int,
                    x:      np.ndarray,
                    L:      int,
                    t:      int,
                    alpha:  float) -> np.ndarray:
    u: np.ndarray = np.zeros_like(x)
    for n in range(1, N + 1):
        u += b_n(L, n) * np.exp(-alpha * (np.pi * n / L) ** 2 * t) * np.sin(np.pi * n * x / L)
    return u

def explicit_method(
                    u_init: np.ndarray,
                    L:      int,
                    t:      float,
                    dx:     float,
                    dt:     float,
                    alpha:  float) -> np.ndarray:
    M: int = int(t / dt)
    N: int = int(L / dx) + 1

    u: np.ndarray = np.zeros((M, N))

    u[0] = np.copy(u_init)

    for n in range(M - 1):
        for i in range(1, len(u_init) - 1):
            u[n + 1, i] = u[n, i] + alpha * dt / (dx ** 2) * (u[n, i - 1] - 2 * u[n, i] + u[n, i + 1])
    return u[-1]

def implicit_method(
                    u_init: np.ndarray,
                    L:      int,
                    t:      int,
                    dx:     float,
                    dt:     float,
                    alpha:  float) -> np.ndarray:
    M: int = int(t / dt)
    N: int = int(L / dx) + 1

    u: np.ndarray = np.zeros((M, N))

    u[0] = np.copy(u_init)

    r: float = alpha * dt / (dx ** 2)
    A: np.ndarray = -r * np.ones(L - 2)
    B: np.ndarray = (1 + 2 * r) * np.ones(L - 1)
    C: np.ndarray = -r * np.ones(L - 2)

    # Предварительные вычисления
    P: np.ndarray = np.zeros(L - 2)
    Q: np.ndarray = np.zeros(L - 1)
    inv_denominator: float = 1 / B[0]
    P[0] = C[0] * inv_denominator

    for i in range(1, L - 2):
        inv_denominator = 1 / (B[i] - A[i - 1] * P[i - 1])
        P[i] = C[i] * inv_denominator

    for n in range(M - 1):
        d: np.ndarray = u[n, 1:-1]
        Q[0] = d[0] * inv_denominator

        for i in range(1, L - 2):
            Q[i] = (d[i] - A[i - 1] * Q[i - 1]) / (B[i] - A[i - 1] * P[i - 1])

        u[n + 1, -2] = Q[-1]
        for i in range(L - 3, -1, -1):
            u[n + 1, i + 1] = Q[i] - P[i] * u[n + 1, i + 2]

        u[n + 1, 0] = u[n + 1, 1]
        u[n + 1, -1] = u[n + 1, -2]

    return u[-1]
