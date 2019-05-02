

from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.keys import Keys

driver = webdriver.Chrome("G:\yout\chromedriver.exe" )
#driver.get('http://wikipedia.org')

#link = driver.find_elements_by_class_name("central-featured-lang lang4")
newTab = driver.find_elements_by_tag_name("body").send_keys(Keys.CONTROL + "t")