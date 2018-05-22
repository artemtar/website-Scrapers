import requests

url = 'https://app-us.meallogger.com/#/login'
values = {'email': 'artemtarasov89@gmail.com',
          'password': 'q1w2e3r4'}

r = requests.post(url, data=values)
print (r.content)