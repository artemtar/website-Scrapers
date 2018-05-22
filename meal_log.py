import requests
import shutil
import os
import re
from selenium import webdriver
from bs4 import BeautifulSoup as soup
import time

from selenium.webdriver.common.keys import Keys

browser = webdriver.Chrome("G:\google_driver\chromedriver.exe")
url = "https://app-us.meallogger.com/#/login"
browser.get(url)

username = browser.find_element_by_id("email") #username form field
password = browser.find_element_by_name("password") #password form field

username.send_keys("artemtarasov89@gmail.com")
password.send_keys("q1w2e3r4")

submitButton = browser.find_element_by_class_name("btn-success")
submitButton.click()

browser.get("https://app-us.meallogger.com/#/timeline/home")
time.sleep(3)
# innerHTML = browser.execute_script("return document.body.innerHTML")
# page_html = browser.page_source
# page_soup = soup(page_html, "html.parser")
# b = browser.find_element_by_class_name("event-datetime-title-container")
# b.click()
# time.sleep(0.5)
# name = browser.find_element_by_class_name("user-name")



# #browser.switch_to_active_element()
# pop_up = soup(browser.page_source, "html.parser")
#
# p1 = re.compile(r'<span>(.+?)<\/span><\/span><\/div><\/div><div class="event-text">')
# p2 = re.compile(r'src=("https:\/\/images.meallogger.com\/images.+?")')
#
# print(p1.findall(str(pop_up)))
# print(p2.findall(str(pop_up)))
#
# def download_images(dirname, url):
#         response = requests.get(url, stream=True)
#         save_image_to_file(response, dirname, 1)
#         del response
#
# def save_image_to_file(image, dirname, suffix):
#     with open('{dirname}/img_{suffix}.jpg'.format(dirname=dirname, suffix=suffix), 'wb') as out_file:
#         shutil.copyfileobj(image.raw, out_file)
#
# def make_dir(dirname):
#     current_path = os.getcwd()
#     path = os.path.join(current_path, dirname)
#     if not os.path.exists(path):
#         os.makedirs(path)

# containers = page_soup.findAll('div',{"class":"event-datetime-title-container"})
# print(containers[0])
# # for e in containers:
# #     print(e.span.text)
# #     print(e.div.event-photo)
#
# # containers = page_soup.findAll('div',{"class":"event-datetime-title-container"})
# # print(containers[0].div.text)
# # print(containers[0].img.get("src"))

# #timer is here
# start = time.time()
# now = time.time()
# while (now - start) < 5:
#     now = time.time()
#     try:
#         # Action scroll down
#         browser.execute_script("window.scrollTo(0, document.body.scrollHeight);")
#     except:
#         print("here")
#         pass


# #scrolling over elem
# b = browser.find_elements_by_class_name("event-datetime-title-container")
# for e in b:
#     e.click()
#     time.sleep(0.5)
#     webdriver.ActionChains(browser).send_keys(Keys.ESCAPE).perform()
#     time.sleep(0.5)