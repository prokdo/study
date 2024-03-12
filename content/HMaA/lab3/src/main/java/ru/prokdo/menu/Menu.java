package ru.prokdo.menu;

import java.util.Scanner;

import ru.prokdo.fill.Filler;
import ru.prokdo.info.ProblemInfo;
import ru.prokdo.info.ResultInfo;
import ru.prokdo.schedule_problem.AlgorithmType;
import ru.prokdo.schedule_problem.ScheduleSolver;
import ru.prokdo.sort.*;

public final class Menu {
    private Menu() {
    }

    public static void show() {
        Menu.showStartMenu();
    }

    @SuppressWarnings("resource")
    private static void showStartMenu() {
        System.err.println();
        System.out.println("=".repeat(100));

        System.out.println("Эвристические методы и алгоритмы, лабораторная работа №3 \"Теория расписаний\"");
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
            case 1 -> Menu.showFillMenu();
            case 2 -> System.exit(0);
        }
    }

    @SuppressWarnings("resource")
    private static void showFillMenu() {
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

        double[] tasksWeights = Filler.fill(tasksCount, minTaskWeight, maxTaskWeight);

        ProblemInfo problemInfo = new ProblemInfo(processorsCount, tasksCount, tasksWeights);

        System.out.println("=".repeat(100));

        Menu.showAlgorithmMenu(problemInfo);
    }

    @SuppressWarnings("resource")
    private static void showAlgorithmMenu(ProblemInfo problemInfo) {
        System.out.println();
        System.out.println("=".repeat(100));

        System.out.println("Выбор алгоритма на исполнение");

        System.out.println();
        System.out.println(problemInfo);

        System.out.println();
        System.out.println("Выберите алгоритм решения задачи:");
        System.out.println("[ 1 ] Метод Крона");
        System.out.println("[ 2 ] Модифицированный метод Крона");
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
            case 1 -> Menu.showSolveMenu(problemInfo, AlgorithmType.CRON_METHOD);
            case 2 -> Menu.showSortMenu(problemInfo);
        }

    }

    @SuppressWarnings("resource")
    private static void showSortMenu(ProblemInfo problemInfo) {
        System.out.println();
        System.out.println("=".repeat(100));

        System.out.println("Выбор сортировки списка задач");

        System.out.println();
        System.out.println(problemInfo);

        System.out.println();
        System.out.println("Выберите порядок сортировки массива задач:");
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
            case 1 -> Sorter.sort(problemInfo.tasksWeights, SortType.ASCENDING);
            case 2 -> Sorter.sort(problemInfo.tasksWeights, SortType.DESCENDING);
            case 3 -> Sorter.sort(problemInfo.tasksWeights, SortType.SHAKE);
        }

        System.out.println("=".repeat(100));
        System.out.println();

        Menu.showSolveMenu(problemInfo, AlgorithmType.CRON_METHOD_MODIFICATION);
    }

    private static void showSolveMenu(ProblemInfo problemInfo, AlgorithmType algorithmType) {
        System.out.println();
        System.out.println("=".repeat(100));

        System.out.println();
        System.out.println(problemInfo);

        System.out.println("=".repeat(100));

        Menu.showResultMenu(problemInfo, ScheduleSolver.solve(problemInfo, algorithmType));
    }

    @SuppressWarnings("resource")
    private static void showResultMenu(ProblemInfo problemInfo, ResultInfo resultInfo) {
        System.out.println();
        System.out.println("=".repeat(100));

        System.out.println("Решение задачи завершено");

        System.out.println();
        System.err.println(resultInfo);

        System.out.println();

        System.out.println();
        System.out.println("Выберите дальнейшие действия:");
        System.out.println("[ 1 ] Вернуться в меню выбора алгоритма");
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
            case 1 -> Menu.showAlgorithmMenu(problemInfo);
            case 2 -> System.exit(0);
        }
    }
}
