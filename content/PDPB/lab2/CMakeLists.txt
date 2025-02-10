# Минимальная версия CMake
cmake_minimum_required(VERSION 3.31)

# Название проекта
project(PDPB_lab2 LANGUAGES CXX)

# Установка стандартов C++ и флагов компиляции
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

# Добавление директории с заголовочными файлами
include_directories(${CMAKE_SOURCE_DIR}/include)

# Поиск всех исходных файлов в папке src рекурсивно
file(GLOB_RECURSE SOURCES CONFIGURE_DEPENDS ${CMAKE_SOURCE_DIR}/src/*.cpp)

# Установка флагов компилятора
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}")

# Определение исполняемого файла
add_executable(PDPB_lab2 ${SOURCES})

# Указание выходной директории для исполняемого файла
set_target_properties(PDPB_lab2 PROPERTIES
    RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/build
)

# Цель для запуска программы
add_custom_target(run
    COMMAND PDPB_lab2
    WORKING_DIRECTORY ${CMAKE_BINARY_DIR}/build
    DEPENDS PDPB_lab2
    COMMENT "Running the program..."
)

# Цель для очистки
add_custom_target(clean-all
    COMMAND ${CMAKE_COMMAND} -E remove_directory ${CMAKE_BINARY_DIR}/build
    COMMAND ${CMAKE_COMMAND} -E remove_directory ${CMAKE_BINARY_DIR}/CMakeFiles
    COMMAND ${CMAKE_COMMAND} -E remove ${CMAKE_BINARY_DIR}/CMakeCache.txt
    COMMENT "Cleaning all generated files..."
)