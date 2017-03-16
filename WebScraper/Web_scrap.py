c# Flight Web Scraper - BRF
	# Using 'BeautifulSoap4' and 'selenium' packages


from bs4 import BeautifulSoup
from selenium import webdriver
import requests
import time



url = 'https://www.skyscanner.net/'
urlRoundTrip = 'https://www.skyscanner.net/transport/flights-from/nyca/cheapest-flights-from-new-york.html?adults=1&children=0&adultsv2=1&childrenv2=&infants=0&cabinclass=economy&rtn=1&preferdirects=false&outboundaltsenabled=false&inboundaltsenabled=false&ref=flights_seo_landing_page'
urlOneWay = 'https://www.skyscanner.net/transport/flights-from/nyca/cheapest-flights-from-new-york.html?adults=1&children=0&adultsv2=1&childrenv2=&infants=0&cabinclass=economy&rtn=0&preferdirects=false&outboundaltsenabled=false&inboundaltsenabled=false&ref=home'
headers = {'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.75.14 (KHTML, like Gecko) Version/7.0.3 Safari/7046A194A'}
# r  = requests.get(url, headers=headers) data = r.text -- COULD NOT GET TO WORK
# browser = webdriver.Chrome('/Users/Terminator/Desktop/chromedriver') -- CHROME GOT STOPPED UP BY SITE

## WORKS
# browser = webdriver.Safari()
# browser.get(urlOneWay)
# time.sleep(5)
# html = browser.page_source

 
# soup = BeautifulSoup(html, 'html.parser') 

## Write HTML output to file so we don't have to scrape site every time
# htmlOutput = soup.prettify("utf-8")
# with open("output.html", "wb") as file:
#     file.write(htmlOutput)

htmlOutput = open("output.html", 'r')


soup = BeautifulSoup(htmlOutput, 'html.parser') 

# print soup.prettify()

tag = soup.find_all("li", class_="browse-list-category")

for t in tag:
	# print t
	cityList = t.find_all("ul", class_="city-list everywhere")
	print cityList

	line = t.contents[1].getText() # get text inbetween the div tags
	line = line.strip().split("\n")
	
	# print line[0] # country
	# print line [-1].strip() # lowest airfare price
	# print '\n'


		# <ul class="city-list everywhere">








