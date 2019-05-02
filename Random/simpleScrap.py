from bs4 import BeautifulSoup as soup
from urllib.request import urlopen as uReq

my_url = 'https://www.nix.ru/autocatalog/nix_computers/BTC-6-GTX1080T-BTC6DLBi-Celeron-G3930-4-Gb-128-Gb-SSD-6x11-Gb-GeForce-GTX1080Ti_339619.html'

uClient = uReq(my_url) ## download webpage
page_html = uClient.read() ## offloads page
uClient.close()
page_soup = soup(page_html, "html.parser")

containers = page_soup.findAll('div', {"class":"goods_like"})
for e in containers:
    print(e.b)