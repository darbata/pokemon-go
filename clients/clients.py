import requests
import random
import time

url = "http://localhost:8080/api/login"

while(True):
    payload = {
        # generate random coordinates of up to 2 decimal places
        "x": random.randint(0, 3),
        "y": random.randint(0, 3)
    }

    # send a request every 1 seconds
    time.sleep(1)

    response = requests.post(url, json=payload)
    print("User logged in at: " + str(payload))
