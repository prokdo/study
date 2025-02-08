# Минимальная версия CMake
cmake_minimum_required(VERSION 3.31)

# Название проекта
project(PDPB_lab1 LANGUAGES CXX)

# Установка стандартов C++ и флагов компиляции
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

# Добавление директории с заголовочными файлами
include_directories(${CMAKE_SOURCE_DIR}/include)

# Поиск всех исходных файлов в папке src рекурсивно
file(GLOB_RECURSE SOURCES CONFIGURE_DEPENDS ${CMAKE_SOURCE_DIR}/src/*.cpp)

# Включение оптимизации и SIMD-инструкций
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O3 -mavx2")

# Определение исполняемого файла
add_executable(PDPB_lab1 ${SOURCES})

# Указание выходной директории для исполняемого файла
set_target_properties(PDPB_lab1 PROPERTIES
    RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/build
)

# Цель для запуска программы
add_custom_target(run
    COMMAND PDPB_lab1
    WORKING_DIRECTORY ${CMAKE_BINARY_DIR}/build
    DEPENDS PDPB_lab1
    COMMENT "Running the program..."
)

# Цель для очистки
add_custom_target(clean-all
    COMMAND ${CMAKE_COMMAND} -E remove_directory ${CMAKE_BINARY_DIR}/build
    COMMAND ${CMAKE_COMMAND} -E remove_directory ${CMAKE_BINARY_DIR}/CMakeFiles
    COMMAND ${CMAKE_COMMAND} -E remove ${CMAKE_BINARY_DIR}/CMakeCache.txt
    COMMENT "Cleaning all generated files..."
)