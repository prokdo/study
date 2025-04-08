import os

if __name__ == '__main__':
    while True:
        print("App name: ", os.getenv("APP_NAME"))
        print("API key: ", os.getenv("API_KEY"))
