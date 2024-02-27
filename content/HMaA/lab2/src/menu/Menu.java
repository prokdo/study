package menu;

import java.util.Scanner;

import fill.Filler;
import schedule_problem.HeterogeneousScheduleSolver;
import sort.SortType;
import util.Matrix;

public final class Menu {
    private Object[] problemInfo = new Object[2];

    public Menu() {
    }

    public void show() {
        this.showStartMenu();
    }

    @SuppressWarnings("resource")
    private void showStartMenu() {
        System.err.println();
        System.out.println("=".repeat(100));

        System.out.println("Эвристические методы и алгоритмы, лабораторная работа №2 \"Теория разнородных расписаний\"");
        System.out.println("Автор: ВПР32, Прокопенко Дмитрий");

        System.out.println();
        System.out.println("Выберите опцию на исполнение:");
        System.out.println("[ 1 ] Ввод исходных данных");
        System.out.println("[ 2 ] Завершение работы");
        System.out.println();
        System.out.print("Ожидание команды: ");

        int choose = -1;
        while (true) {
            try {
                choose = new Scanner(System.in).nextInt();

                if (choose < 1 || choose > 2)
                    throw new Exception();
            } catch (Exception exception) {
                System.out.println();
                System.out.print("Команда не распознана. Повторите ввод: ");

                continue;
            }
            break;
        }
        System.out.println("=".repeat(100));
        System.out.println();

        switch (choose) {
            case 1 -> this.showFillMenu();
            case 2 -> System.exit(0);
        }
    }

    @SuppressWarnings("resource")
    private void showFillMenu() {
        System.out.println("=".repeat(100));
        System.out.println("Ввод данных");

        System.out.println();
        System.out.print("Введите количество процессоров (N): ");
        int processorsCount = -1;
        while (true) {
            try {
                processorsCount = new Scanner(System.in).nextInt();

                if (processorsCount <= 0)
                    throw new Exception();
            } catch (Exception exception) {
                System.out.println();
                System.out.print("Неверный ввод. Попробуйте ещё раз: ");

                continue;
            }
            break;
        }

        System.out.print("Введите количество задач (M): ");
        int tasksCount = -1;
        while (true) {
            try {
                tasksCount = new Scanner(System.in).nextInt();

                if (tasksCount <= 0)
                    throw new Exception();
            } catch (Exception exception) {
                System.out.println();
                System.out.print("Неверный ввод. Попробуйте ещё раз: ");

                continue;
            }
            break;
        }

        System.out.print("Введите диапазон весов задач (T_min, T_max): ");
        int minTaskWeight = -1;
        int maxTaskWeight = -1;
        while (true) {
            try {
                String[] str = new Scanner(System.in).nextLine().split(", ");
                minTaskWeight = Integer.parseInt(str[0]);
                maxTaskWeight = Integer.parseInt(str[1]);

                if (minTaskWeight <= 0 || maxTaskWeight <= 0)
                    throw new Exception();

                if (minTaskWeight > maxTaskWeight) {
                    int temp = maxTaskWeight;
                    maxTaskWeight = minTaskWeight;
                    minTaskWeight = temp;
                }
            } catch (Exception exception) {
                System.out.println();
                System.out.print("Неверный ввод. Попробуйте ещё раз: ");

                continue;
            }
            break;
        }

        this.problemInfo[0] = Filler.fill(tasksCount, processorsCount, minTaskWeight, maxTaskWeight);

        System.out.println();
        System.out.println(String.format("Матрица весов задач (T):\n%s", (Matrix) problemInfo[0]));

        System.out.println("=".repeat(100));

        this.showSortMenu();
    }

    @SuppressWarnings("resource")
    private void showSortMenu() {
        System.out.println();
        System.out.println("=".repeat(100));

        System.out.println("Выбор сортировки строк матрицы");

        this.showProblemInfo();

        System.out.println();
        System.out.println("Выберите порядок сортировки строк матрицы весов задач:");
        System.out.println("[ 1 ] По возрастанию");
        System.out.println("[ 2 ] По убыванию");
        System.out.println("[ 3 ] Случайно");
        System.out.println();
        System.out.print("Ожидание команды: ");

        int choose = -1;
        while (true) {
            try {
                choose = new Scanner(System.in).nextInt();

                if (choose < 1 || choose > 3)
                    throw new Exception();
            } catch (Exception exception) {
                System.out.println();
                System.out.print("Команда не распознана. Повторите ввод: ");

                continue;
            }
            break;
        }

        switch (choose) {
            case 1 -> this.problemInfo[1] = SortType.ASCENDING;
            case 2 -> this.problemInfo[1] = SortType.DESCENDING;
            case 3 -> this.problemInfo[1] = SortType.SHAKE;
        }

        System.out.println("=".repeat(100));
        System.out.println();

        System.out.println("Производится расчет решения задачи . . .");

        this.showResultMenu(HeterogeneousScheduleSolver.solve(problemInfo));
    }

    @SuppressWarnings("resource")
    private void showResultMenu(Object[] result) {
        System.out.println();
        System.out.println("=".repeat(100));

        System.out.println("Решение задачи завершено");

        this.showProblemInfo();

        System.out.println();
        System.out.println("Результат работы алгоритма:");
        System.out.println(String.format("Матрица нагрузки процессоров: \n%s", (Matrix) result[0]));
        System.out.println(String.format("Максимальная нагрузка на процессор: %.0f", (double) result[1]));
        System.out.println(String.format("Время работы алгоритма (мс): %d", (int) ((long) result[2] / 1000)));

        System.out.println();
        System.out.println("Выберите дальнейшие действия:");
        System.out.println("[ 1 ] Вернуться в меню выбора сортировки");
        System.out.println("[ 2 ] Завершение работы");
        System.out.println();
        System.out.print("Ожидание команды: ");

        int choose = -1;
        while (true) {
            try {
                choose = new Scanner(System.in).nextInt();

                if (choose < 1 || choose > 2)
                    throw new Exception();
            } catch (Exception exception) {
                System.out.println();
                System.out.print("Команда не распознана. Повторите ввод: ");

                continue;
            }
            break;
        }

        switch (choose) {
            case 1 -> this.showSortMenu();
            case 2 -> System.exit(0);
        }

        System.out.println("=".repeat(100));
        System.out.println();
    }

    private void showProblemInfo() {
        System.out.println();
        System.out.println("Характеристика задачи:");

        System.out.println(String.format("Количество процессоров (N): %d", ((Matrix) this.problemInfo[0]).getColumnsCount()));
        System.out.println(String.format("Количество задач (M): %d", ((Matrix) this.problemInfo[0]).getRowsCount()));
        System.out.println(String.format("Матрица весов задач (T):\n%s", (Matrix) problemInfo[0]));
    }
}
