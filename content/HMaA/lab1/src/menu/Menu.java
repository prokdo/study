package menu;

import java.util.Scanner;

import fill.Filler;
import schedule_problem.AlgorithmType;
import schedule_problem.ScheduleSolver;
import sort.SortType;
import sort.Sorter;
import util.Matrix;

public final class Menu {
    private Object[] problemInfo = new Object[4];

    public Menu() {
    }

    public void show() {
        this.showStartMenu();
    }

    @SuppressWarnings("resource")
    private void showStartMenu() {
        System.err.println();
        System.out.println("=".repeat(100));

        System.out.println("Эвристические методы и алгоритмы, лабораторная работа №1 \"Теория расписаний\"");
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
        this.problemInfo[2] = processorsCount;

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
        this.problemInfo[1] = tasksCount;

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

        this.problemInfo[0] = Filler.fill((int) problemInfo[1], minTaskWeight, maxTaskWeight);

        System.out.println();
        this.printTasksArray();

        System.out.println("=".repeat(100));

        this.showAlgorithmMenu();
    }

    @SuppressWarnings("resource")
    private void showAlgorithmMenu() {
        System.out.println();
        System.out.println("=".repeat(100));

        System.out.println("Выбор алгоритма на исполнение");

        this.showProblemInfo();

        System.out.println();
        System.out.println("Выберите алгоритм решения задачи:");
        System.out.println("[ 1 ] Метод критического пути (CMP)");
        System.out.println("[ 2 ] Метод половинного деления множества задач (HDMT)");
        System.out.println("[ 3 ] Упорядоченное разбиение множества задач (OFMT)");
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
        System.out.println("=".repeat(100));
        System.out.println();

        switch (choose) {
            case 1 -> this.showCMPMenu();
            case 2 -> this.showSolveMenu(AlgorithmType.HALF_DIVISION_MULTITUDE_TASKS);
            case 3 -> this.showSolveMenu(AlgorithmType.ORDERED_FRAGMENTATION_MULTITUDE_TASKS);
        }

    }

    @SuppressWarnings("resource")
    private void showCMPMenu() {
        System.out.println();
        System.out.println("=".repeat(100));

        System.out.println("Выбор сортировки списка для метода критического пути");

        this.showProblemInfo();

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
            case 1 -> this.problemInfo[3] = SortType.ASCENDING;
            case 2 -> this.problemInfo[3] = SortType.DESCENDING;
            case 3 -> this.problemInfo[3] = SortType.SHAKE;
        }

        System.out.println();
        this.printTasksArray();

        System.out.println("=".repeat(100));
        System.out.println();

        this.showSolveMenu(AlgorithmType.CRITICAL_METHOD_PATH);
    }

    private void showSolveMenu(AlgorithmType algorithmType) {
        System.out.println();
        System.out.println("=".repeat(100));

        this.showProblemInfo();

        switch (algorithmType) {
            case CRITICAL_METHOD_PATH -> {
                Sorter.sort((Double[]) this.problemInfo[0], (SortType) this.problemInfo[3]);
                this.showResultMenu(ScheduleSolver.solve(this.problemInfo, AlgorithmType.CRITICAL_METHOD_PATH));
            }
            case HALF_DIVISION_MULTITUDE_TASKS -> {
                if ((int) this.problemInfo[2] % 2 != 0)
                    this.showFailMenu(AlgorithmType.HALF_DIVISION_MULTITUDE_TASKS);

                Sorter.sort((Double[]) this.problemInfo[0], SortType.DESCENDING);
                this.showResultMenu(
                        ScheduleSolver.solve(this.problemInfo, AlgorithmType.HALF_DIVISION_MULTITUDE_TASKS));
            }
            case ORDERED_FRAGMENTATION_MULTITUDE_TASKS -> {
                if ((int) (Math.ceil((Math.log((int) this.problemInfo[2]) / Math.log(2)))) != (int) (Math
                        .floor(((Math.log((int) this.problemInfo[2]) / Math.log(2))))))
                    this.showFailMenu(AlgorithmType.ORDERED_FRAGMENTATION_MULTITUDE_TASKS);

                Sorter.sort((Double[]) this.problemInfo[0], SortType.DESCENDING);
                this.showResultMenu(
                        ScheduleSolver.solve(this.problemInfo, AlgorithmType.ORDERED_FRAGMENTATION_MULTITUDE_TASKS));
            }
        }
    }

    @SuppressWarnings("resource")
    private void showFailMenu(AlgorithmType algorithmType) {
        System.out.println();
        System.out.println("=".repeat(100));

        System.out.println("Ошибка в работе алгоритма");

        this.showProblemInfo();

        switch (algorithmType) {
            case HALF_DIVISION_MULTITUDE_TASKS -> {
                System.out.println("Алгоритм: HDMT");
                System.out.println();
                System.out.println("Причина ошибки: количество процессоров не является чётным числом");
            }
            case ORDERED_FRAGMENTATION_MULTITUDE_TASKS -> {
                System.out.println("Алгоритм: OFMT");
                System.out.println();
                System.out.println("Причина ошибки: количество процессоров не является степенью 2");
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + algorithmType);
        }

        System.out.println();
        System.out.println("Выберите дальнейшие действия:");
        System.out.println("[ 1 ] Вернуться к выбору алгоритма решения");
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
            case 1 -> this.showAlgorithmMenu();
            case 2 -> System.exit(0);
        }
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
        System.out.println("[ 1 ] Вернуться в главное меню");
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
            case 1 -> this.showStartMenu();
            case 2 -> System.exit(0);
        }

        System.out.println("=".repeat(100));
        System.out.println();
    }

    private void showProblemInfo() {
        System.out.println();
        System.out.println("Характеристика задачи:");

        System.out.println(String.format("Количество процессоров (N): %d", this.problemInfo[2]));
        System.out.println(String.format("Количество задач (M): %d", this.problemInfo[1]));
        this.printTasksArray();
    }

    private void printTasksArray() {
        System.out.print("Список задач (T): [");
        for (int i = 0; i < (int) this.problemInfo[1]; i++) {
            if (i == (int) this.problemInfo[1] - 1)
                System.out.println(String.format("%.0f]", ((Double[]) this.problemInfo[0])[i]));
            else {
                System.out.print(String.format("%.0f, ", ((Double[]) this.problemInfo[0])[i]));
            }
        }
    }
}
