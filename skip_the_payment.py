
# coding: utf-8

# In[20]:


import shutil
import os
import re
from selenium import webdriver
from bs4 import BeautifulSoup as soup
import time
from selenium.webdriver.common.keys import Keys
import random
from random_word import RandomWords


# In[24]:


r = RandomWords()


# In[34]:


def get_email():
    word1 = r.get_random_word()
    word2 = r.get_random_word()
    rand_int = random.randint(000, 999)
    return word1 + "_" + word2 + str(rand_int) + "@gmail.com"


# In[33]:


get_email()


# In[2]:


alphabet = "A a B b C c D d E e F f G g H h I i J j K k L l M m N n O o P p Q q R r S s T t U u V v W w X x Y y Z z"
int_to_let = {i:l for i, l in enumerate(set(alphabet))}
def convert(integer):
    result = ""
    for i in integer:
        result = result + int_to_let[int(i)]
    return result


# In[16]:


rand_int = random.randint(111111, 999999)
int_let = convert(str(rand_int))
my_name = "Artem" + int_let
new_email = "emaaail" + str(rand_int) + "@mailinator.com"
rand_int2 = random.randint(1111110, 9999990)
phone_num = "613" + str(rand_int2)
input_password = "veryHardPassword!1"
address = "419 McLeod Street, Ottawa, ON"


# In[4]:


invite_dishes = "https://www.skipthedishes.com/r/OBzNJ9Du6e"
mailnator_login = "https://www.mailinator.com/"


# In[10]:


driver_path = "/home/atarasov/Downloads/driver/chromedriver"
dishes = webdriver.Chrome(driver_path)
mailnator = webdriver.Chrome(driver_path)


# In[11]:


dishes.get(invite_dishes)
mailnator.get(mailnator_login)


# In[14]:


login_form = dishes.find_element_by_id("nav-dropdown-account")
login_form.click()
time.sleep(0.3)
sign_up = dishes.find_element_by_link_text("Need an account? Sign up")
sign_up.click()


# In[17]:


name = dishes.find_element_by_id("name")
phone = dishes.find_element_by_id("phone_number") 
email = dishes.find_element_by_id("email")
password = dishes.find_element_by_id("password")
password_conf = dishes.find_element_by_id("confirm_password") 

name.send_keys(my_name)
phone.send_keys(phone_num)
email.send_keys(new_email)
password.send_keys(input_password)
password_conf.send_keys(input_password)

log_in = dishes.find_element_by_id("submit-btn")
log_in.click()


# In[18]:


adress = dishes.find_element_by_name("address_display")
adress.send_keys(address)
popup = dishes.find_element_by_name("address_display")
time.sleep(0.3)
popup.send_keys(Keys.DOWN)
time.sleep(0.5)
delivery = dishes.find_element_by_class_name("selected")
delivery.click()


# In[9]:


restarants = dishes.find_element_by_class_name("address-google-map-button")
restarants.click()

