# Taxi Fleet Administration Web Application

## Overview

This web application serves as a comprehensive platform for managing a taxi fleet. It allows administrators to manage drivers and vehicles efficiently while providing drivers with a user-friendly interface to monitor their trips, take vehicles, and manage their activities. The application is built using modern web technologies, ensuring a smooth and responsive user experience.

## Features

- **Driver Management**: Admins can add, remove, and update driver information.
- **Vehicle Management**: Manage vehicles in the fleet, including assigning them to drivers.
- **Trip Management**: Drivers can view current trips, take vehicles, start, and complete trips.
- **Trip History**: Drivers can access their trip history with filtering options.
- **User Authentication**: Secure (but not really, though can be improved) login for both administrators and drivers.

## Technology Stack

- **Backend**: Java 21, Spring (Spring Boot, Spring Data JPA, Spring Security), Lombok.
- **Frontend**: Thymeleaf for rendering views.
- **Database**: PostgreSQL 16.4 for data storage.

## Database structure

The database consists of the following tables:

1. **drivers**
   - `id`: SERIAL PRIMARY KEY.
   - `first_name`: VARCHAR(50) NOT NULL.
   - `last_name`: VARCHAR(50) NOT NULL.
   - `surname`: VARCHAR(50).
   - `phone`: VARCHAR(20) UNIQUE NOT NULL.
   - `birth_date`: DATE NOT NULL.
   - `address`: TEXT NOT NULL.
   - `hire_date`: DATE NOT NULL.
   - `is_free`: BOOLEAN DEFAULT TRUE NOT NULL.
   - `license_number`: VARCHAR(255) UNIQUE NOT NULL.

2. **cars**
   - `id`: SERIAL PRIMARY KEY.
   - `make`: VARCHAR(50) NOT NULL.
   - `model`: VARCHAR(50) NOT NULL.
   - `license_plate`: VARCHAR(15) UNIQUE NOT NULL.
   - `driver_id`: INTEGER FOREIGN KEY REFERENCES drivers(id) UNIQUE.
   - `registratrion_date`: DATE NOT NULL.

3. **trips**
   - `id`: SERIAL PRIMARY KEY.
   - `driver_id`: INTEGER FOREIGN KEY REFERENCES drivers(id) UNIQUE NOT NULL.
   - `car_id` INTEGER FOREIGN KEY REFERENCES cars(id) UNIQUE NOT NULL.
   - `start_time`: TIMESTAMP NOT NULL.
   - `end_time`: TIMESTAMP.
   - `start_location`: VARCHAR(255) NOT NULL.
   - `end_location`: VARCHAR(255) NOT NULL.

4. **administrators**
  - `id`: SERIAL PRIMARY KEY.
   - `first_name`: VARCHAR(50) NOT NULL.
   - `last_name`: VARCHAR(50) NOT NULL.
   - `surname`: VARCHAR(50).
   - `phone`: VARCHAR(20) UNIQUE NOT NULL.
   - `email`: VARCHAR(100) UNIQUE NOT NULL.
   - `password_hash`: VARCHAR(255) NOT NULL.

---

**Note:** This program is created ONLY for educational purposes.
