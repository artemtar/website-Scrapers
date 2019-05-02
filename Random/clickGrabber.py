import re
import linkGrabber
from selenium import webdriver
import time
# links = linkGrabber.Links("http://google.com")
# gb = links.find(limit=4, pretty=True)
# print(gb)

driver = webdriver.Chrome(executable_path="/usr/bin/chromium-browser")

driver.get("http://onecore.net")
ids = driver.find_elements_by_xpath("//*[@href]")


for i in ids:
    print(i.get_attribute('href'))

time.sleep(4)
driver.close()