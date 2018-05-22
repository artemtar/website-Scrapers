from bs4 import BeautifulSoup as soup
from selenium import webdriver
url = ('https://app-us.meallogger.com/#/login')
username = 'artemtarasov89@gmail.com'
pasw = 'zaq1w2e3r4'


driver = webdriver.Chrome(executable_path="/usr/bin/chromium-browser")
#driver.get('https://app-us.meallogger.com/#/login')
