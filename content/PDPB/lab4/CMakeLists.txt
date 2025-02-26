# Минимальная версия CMake
cmake_minimum_required(VERSION 3.10)

# Название проекта
project(PDPB_lab4 LANGUAGES CXX)

# Установка стандартов C++ и флагов компиляции
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

# Добавление директории с заголовочными файлами
include_directories(${CMAKE_SOURCE_DIR}/include)

# Поиск всех исходных файлов в папке src рекурсивно
file(GLOB_RECURSE SOURCES CONFIGURE_DEPENDS ${CMAKE_SOURCE_DIR}/src/*.cpp)

# Установка флагов компилятора
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fopenmp -O3")

# Определение исполняемого файла
add_executable(PDPB_lab4 ${SOURCES})

# Указание выходной директории для исполняемого файла
set_target_properties(PDPB_lab4 PROPERTIES
    RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/build
)

# Указание необходимых пакетов зависимостей
find_package(OpenMP REQUIRED)

# Цель для запуска программы
add_custom_target(run
    COMMAND PDPB_lab4
    WORKING_DIRECTORY ${CMAKE_BINARY_DIR}/build
    DEPENDS PDPB_lab4
    COMMENT "Running the program..."
)

# Цель для очистки
add_custom_target(clean-all
    COMMAND ${CMAKE_COMMAND} -E remove_directory ${CMAKE_BINARY_DIR}/build
    COMMAND ${CMAKE_COMMAND} -E remove_directory ${CMAKE_BINARY_DIR}/CMakeFiles
    COMMAND ${CMAKE_COMMAND} -E remove ${CMAKE_BINARY_DIR}/CMakeCache.txt
    COMMENT "Cleaning all generated files..."
)
